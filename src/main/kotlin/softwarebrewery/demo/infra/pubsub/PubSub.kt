package softwarebrewery.demo.infra.pubsub

import com.google.cloud.pubsub.v1.*
import com.google.cloud.spring.pubsub.core.*
import com.google.cloud.spring.pubsub.core.subscriber.*
import com.google.cloud.spring.pubsub.support.*
import mu.*
import org.springframework.context.*
import softwarebrewery.demo.infra.pubsub.PubSubSubscriber.PubSubMessageHandler.*
import java.nio.*
import java.util.concurrent.TimeUnit.*
import java.util.function.*

typealias Subscription = String

interface MessageHandler {
    operator fun invoke(attributes: Map<String, String>, data: ByteBuffer)
    fun name(): String = javaClass.simpleName
}

class PubSubSubscriber(
    private val subscriberOps: PubSubSubscriberOperations,
    private val subscription: Subscription,
    private val handler: BiConsumer<Subscription, BasicAcknowledgeablePubsubMessage>,
) : SmartLifecycle {

    private val log = KotlinLogging.logger { }
    private var subscriber: Subscriber? = null

    override fun start() {
        if (subscriber != null) return
        log.info { "Handler '$subscription/$handler' starting..." }
        subscriber = subscriberOps.subscribe(subscription) { handler.accept(subscription, it) }
        log.info { "Handler '$subscription/$handler' started" }
    }

    override fun stop() {
        if (subscriber == null) return
        try {
            log.info { "Handler '$subscription/$handler' stopping..." }
            subscriber?.stopAsync()?.awaitTerminated(5, SECONDS)
            log.info { "Handler '$subscription/$handler' stopped" }
        } catch (ex: Throwable) {
            log.warn(ex) { "Handler '$subscription/$handler' failed to stop..." }
        }
    }

    override fun isRunning(): Boolean = subscriber?.isRunning ?: false

    class Factory(
        private val pubSubOps: PubSubOperations,
        private val handleFailure: (HandlingFailed) -> Unit,
    ) {

        fun newSubscriber(
            subscription: String,
            handler: MessageHandler,
        ) = PubSubSubscriber(
            subscriberOps = pubSubOps,
            subscription = subscription,
            handler = PubSubMessageHandler(
                handleMessage = handler,
                handleFailure = handleFailure,
            ),
        )
    }

    class PubSubMessageHandler(
        private val handleMessage: MessageHandler,
        private val handleFailure: (HandlingFailed) -> Unit,
        private val log: KLogger = KotlinLogging.logger { }
    ) : (String, BasicAcknowledgeablePubsubMessage) -> Unit {

        override fun invoke(subscription: String, message: BasicAcknowledgeablePubsubMessage) {
            try {
                handleMessage(message.pubsubMessage.attributesMap, message.pubsubMessage.data.asReadOnlyByteBuffer())
                message.ack()
            } catch (ex: Throwable) {
                handleFailure(
                    HandlingFailed(
                        cause = ex,
                        handler = handleMessage,
                        subscription = subscription,
                        message = message,
                        log = log,
                    )
                )
            }
        }

        class HandlingFailed(
            val handler: MessageHandler,
            val cause: Throwable,
            val subscription: Subscription,
            val message: BasicAcknowledgeablePubsubMessage,
            val log: KLogger,
        )
    }
}

val LogErrorAndDropMessage: (HandlingFailed) -> Unit = {
    it.log.error(it.cause) {
        "${it.subscription}/${it.handler.name()} dropped '${it.message}'"
    }
    it.message.ack()
}
