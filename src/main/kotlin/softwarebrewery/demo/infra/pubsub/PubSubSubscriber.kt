package softwarebrewery.demo.infra.pubsub

import com.google.cloud.pubsub.v1.*
import com.google.cloud.spring.pubsub.core.subscriber.*
import com.google.cloud.spring.pubsub.support.*
import mu.*
import org.springframework.context.*
import softwarebrewery.demo.infra.pubsub.PubSubSubscriber.*
import java.nio.*
import java.util.concurrent.TimeUnit.*

typealias Subscription = String

fun interface NewPubSubSubscriber {
    operator fun invoke(subscription: Subscription, handler: MessageHandler) : PubSubSubscriber
}

interface MessageHandler {
    operator fun invoke(attributes: Map<String, String>, data: ByteBuffer)
    fun name(): String = javaClass.simpleName
}

class PubSubSubscriber(
    private val subscriberOps: PubSubSubscriberOperations,
    private val subscription: Subscription,
    private val handleMessage: MessageHandler,
    private val handleFailure: (HandlingFailed) -> Unit,
) : SmartLifecycle {

    private val log = KotlinLogging.logger { }
    private val name = "$subscription/${handleMessage.name()}"

    private var subscriber: Subscriber? = null

    override fun start() {
        if (subscriber != null) return
        log.info { "Subscriber '$name' starting..." }
        subscriber = subscriberOps.subscribe(subscription) {
            try {
                handleMessage(it.pubsubMessage.attributesMap, it.pubsubMessage.data.asReadOnlyByteBuffer())
                it.ack()
            } catch (ex: Throwable) {
                handleFailure(
                    HandlingFailed(
                        cause = ex,
                        handler = handleMessage,
                        subscription = subscription,
                        message = it,
                        log = log,
                    )
                )
            }
        }
        log.info { "Subscriber '$name' started" }
    }

    override fun stop() {
        if (subscriber == null) return
        try {
            log.info { "Subscriber '$name' stopping..." }
            subscriber?.stopAsync()?.awaitTerminated(5, SECONDS)
            log.info { "Subscriber '$name' stopped" }
        } catch (ex: Throwable) {
            log.warn(ex) { "Subscriber '$name' failed to stop..." }
        }
    }

    override fun isRunning(): Boolean = subscriber?.isRunning ?: false

    class HandlingFailed(
        val handler: MessageHandler,
        val cause: Throwable,
        val subscription: Subscription,
        val message: BasicAcknowledgeablePubsubMessage,
        val log: KLogger,
    )
}

val LogErrorAndDropMessage: (HandlingFailed) -> Unit = {
    it.log.error(it.cause) {
        "${it.subscription}/${it.handler.name()} dropped '${it.message}'"
    }
    it.message.ack()
}
