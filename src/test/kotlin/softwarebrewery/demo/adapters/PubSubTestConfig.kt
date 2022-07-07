package softwarebrewery.demo.adapters

import com.google.cloud.spring.pubsub.*
import com.google.pubsub.v1.*
import mu.*
import org.springframework.beans.factory.annotation.*
import org.springframework.boot.test.context.*
import javax.annotation.*

private val log = KotlinLogging.logger {  }

@TestConfiguration
class PubSubTestConfig(
    private val pubSubAdmin: PubSubAdmin,
    @Value("\${pubsub.promo.promotion-events.topic}") private val promotionsTopic: String,
    @Value("\${pubsub.promo.promotion-events.subscription}") private val promotionsSubscription: String,
) {
    private val topicSubscriptions = setOf(
        TopicSubscription(promotionsTopic, promotionsSubscription),
    )

    @PostConstruct
    fun recreateTopicSubscriptions() {
        topicSubscriptions.reversed().forEach { pubSubAdmin.deleteSubscriptionIfExists(it.subscription) }
        topicSubscriptions.reversed().forEach { pubSubAdmin.deleteTopicIfExists(it.topic) }
        topicSubscriptions.forEach {
            if (!pubSubAdmin.hasTopic(it.topic)) {
                log.info { "Creating topic '${it.topic}'" }
                pubSubAdmin.createTopic(it.topic)
            }
            if (!pubSubAdmin.hasSubscription(it.subscription)) {
                log.info { "Creating subscription '${it.subscription}'" }
                pubSubAdmin.createSubscription(it.subscription, it.topic)
            }
        }
    }
}

private data class TopicSubscription(val topic: String, val subscription: String)

private fun nameOf(topic: Topic): String = topic.name.substringAfterLast("/")
private fun PubSubAdmin.hasTopic(topic: String) = listTopics().any { nameOf(it) == topic }
private fun PubSubAdmin.deleteTopicIfExists(topic: String) {
    if (hasTopic(topic)) {
        log.info { "Deleting topic '$topic'" }
        deleteTopic(topic)
    }
}
private fun nameOf(subscription: Subscription) = subscription.name.substringAfterLast("/")
private fun PubSubAdmin.hasSubscription(subscription: String) = listSubscriptions().any { nameOf(it) == subscription }
private fun PubSubAdmin.deleteSubscriptionIfExists(subscription: String) {
    if (hasSubscription(subscription)) {
        log.info { "Deleting subscription '$subscription'" }
        deleteSubscription(subscription)
    }
}
