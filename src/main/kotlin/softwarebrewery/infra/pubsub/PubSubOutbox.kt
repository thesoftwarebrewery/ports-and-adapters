package softwarebrewery.infra.pubsub

import softwarebrewery.infra.pubsub.MessageProcessingResult.*
import com.fasterxml.jackson.module.kotlin.*
import com.google.protobuf.*
import com.google.pubsub.v1.*
import kotlinx.coroutines.*
import org.springframework.context.*
import org.springframework.jdbc.core.*
import org.springframework.jdbc.core.namedparam.*
import org.springframework.transaction.*
import org.springframework.transaction.annotation.*
import org.springframework.transaction.annotation.Propagation.*
import org.springframework.transaction.event.*
import org.springframework.transaction.event.TransactionPhase.*
import org.springframework.transaction.support.*
import java.nio.*
import java.time.*
import java.time.Instant.*
import java.util.concurrent.*
import java.util.concurrent.Executors.*
import java.util.concurrent.TimeUnit.*
import kotlin.time.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

fun interface PubSubOutbox {
    fun post(messages: Collection<Message>)
}

fun PubSubOutbox.post(message: Message) = post(listOf(message))

data class Message(
    val id: String,
    val topic: String,
    val orderingKey: String? = null,
    val attributes: Map<String, String>,
    val data: ByteBuffer,
)

open class TransactionalPubSubOutbox(
    private val outboxStorage: OutboxStorage,
    private val eventPublisher: ApplicationEventPublisher,
    private val publishMessage: PubSubPublisher,
) : PubSubOutbox {

    @Transactional(propagation = MANDATORY)
    override fun post(messages: Collection<Message>) {
        outboxStorage.store(messages)
        eventPublisher.publishEvent(MessagesStored(messages))
    }

    @TransactionalEventListener(phase = AFTER_COMMIT)
    open fun afterMessagesStored(event: MessagesStored) {
        event.messages.forEach { m ->
            publishMessage(m.topic, m.toPubSubMessage()).addCallback(
                { outboxStorage.removeById(m.id) },
                { /* ignore publication failure as this was conscious best effort */ })
        }
    }

    class MessagesStored(val messages: Collection<Message>)

}

fun Message.toPubSubMessage(): PubsubMessage {
    val message = PubsubMessage.newBuilder().putAllAttributes(attributes).setData(ByteString.copyFrom(data))
    orderingKey?.let { message.setOrderingKey(it) }
    return message.build()!!
}

interface OutboxStorage {
    fun store(messages: Collection<Message>)
    fun removeById(messageId: String)
    fun removeById(messageIds: Collection<String>)
    fun processWithLease(maxMessages: Int, minMessageAge: Duration, processing: MessageProcessing): Result
}

typealias MessageProcessing = (messages: Collection<Message>) -> Map<Message, MessageProcessingResult>

enum class MessageProcessingResult { FAILED, SUCCEEDED }

data class Result(val wasEmpty: Boolean, val succeeded: Int)

open class JdbcOutboxStorage(
    private val db: NamedParameterJdbcTemplate,
    private val txManager: PlatformTransactionManager,
    private val clock: () -> Instant = ::now,
) : OutboxStorage {

    override fun store(messages: Collection<Message>) {
        val query = """
            insert into outbox_messages (id, topic, ordering_key, attributes, data)
            values (:id, :topic, :ordering_key, :attributes, :data)
        """.trimIndent()
        val params = messages.map {
            mapOf(
                "id" to it.id,
                "topic" to it.topic,
                "ordering_key" to it.orderingKey,
                "attributes" to it.attributes.toJson(),
                "data" to it.data.array(),
            )
        }
        db.batchUpdate(query, params.toTypedArray())
    }

    override fun removeById(messageId: String) {
        db.update("delete from outbox_messages where id = :id", mapOf("id" to messageId))
    }

    override fun removeById(messageIds: Collection<String>) {
        messageIds.chunked(1_000) {
            db.update("delete from outbox_messages where id in (:ids)", mapOf("ids" to it))
        }
    }

    override fun processWithLease(maxMessages: Int, minMessageAge: Duration, processing: MessageProcessing): Result {
        // enable caller of this api to apply message processing to temporary exclusively leased set of messages
        // processing results indicate whether messages should be kept or dropped from storage
        // the 'lease' is active within transaction scope managed within here to alleviate transaction management burden
        // from caller of this api
        val tx = txManager.getTransaction(DefaultTransactionDefinition())
        try {
            val messages = lease(maxMessages, storedBeforeOrAt = clock().minus(minMessageAge.toJavaDuration()))
            if (messages.isEmpty()) return Result(wasEmpty = true, succeeded = 0)
            val processed = processing(messages)
            val succeededIds = processed.messageIdsFor(SUCCEEDED)
            removeById(succeededIds)
            return Result(wasEmpty = false, succeeded = succeededIds.size)
        } catch (ex: Throwable) {
            txManager.rollback(tx)
            throw ex
        } finally {
            // always commit tx to guarantee release of rows locked by our 'lease'
            if (!tx.isCompleted) {
                txManager.commit(tx)
            }
        }
    }

    private fun lease(maxMessages: Int, storedBeforeOrAt: Instant): Collection<Message> {
        val query = """
            select id, topic, ordering_key, attributes, data from outbox_messages
            where created_at <= :stored_before_or_at
            order by created_at
            limit :limit
            for update skip locked
        """.trimIndent()
        val params = mapOf(
            "limit" to maxMessages,
            "stored_before_or_at" to java.sql.Timestamp.from(storedBeforeOrAt),
        )
        return db.query(query, params, messageMapper)
    }

    companion object {

        private val jackson = jacksonObjectMapper()

        private val messageMapper = RowMapper { rs, _ ->
            Message(
                id = rs.getString("id"),
                topic = rs.getString("topic"),
                orderingKey = rs.getString("ordering_key"),
                attributes = jackson.readValue(rs.getString("attributes")),
                data = ByteBuffer.wrap(rs.getBytes("data")),
            )
        }

        private fun Map<String, String>.toJson() = jackson.writeValueAsString(this)

    }

    private fun Map<Message, MessageProcessingResult>.messageIdsFor(result: MessageProcessingResult) =
        filterValues { r -> r == result }.keys.map { m -> m.id }

}

class PubSubOutboxRetryJob(
    private val executor: ScheduledExecutorService = newSingleThreadScheduledExecutor(),
    private val outboxStorage: OutboxStorage,
    private val publishMessage: PubSubPublisher,
    private val runInterval: Duration = 5.seconds,
    private val maxMessagesPerBatch: Int = 1000,
    private val minMessageAge: Duration = 30.seconds,
) : Runnable, SmartLifecycle {

    override fun run() {
        outboxStorage.processWithLease(maxMessagesPerBatch, minMessageAge) { batch ->
            runBlocking {
                batch.map { m ->
                    async {
                        try {
                            withContext(Dispatchers.IO) {
                                publishMessage(m.topic, m.toPubSubMessage()).get()
                                m to SUCCEEDED
                            }
                        } catch (ex: Throwable) {
                            m to FAILED
                        }
                    }
                }.awaitAll().toMap()
            }
        }
    }

    override fun start() {
        executor.scheduleWithFixedDelay(::run, 0, runInterval.inWholeSeconds, SECONDS)
    }

    override fun stop() {
        executor.shutdownNow()
        executor.awaitTermination(5, SECONDS)
    }

    override fun isRunning(): Boolean = executor.isTerminated

}
