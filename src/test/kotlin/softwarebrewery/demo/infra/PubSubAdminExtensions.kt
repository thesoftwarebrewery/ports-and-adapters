package softwarebrewery.demo.infra

import com.google.cloud.spring.pubsub.*
import com.google.pubsub.v1.*
import mu.*

private val log = KotlinLogging.logger { }

fun Topic.simpleName(): String = name.substringAfterLast("/")

fun Subscription.simpleName(): String = name.substringAfterLast("/")

fun PubSubAdmin.hasTopic(topic: String) = listTopics().any { it.simpleName() == topic }

fun PubSubAdmin.createTopicIfNotExists(topic: String) {
    if (hasTopic(topic)) return
    log.info { "Creating topic '${topic}'" }
    createTopic(topic)
}

fun PubSubAdmin.deleteTopicIfExists(topic: String) {
    if (!hasTopic(topic)) return
    log.info { "Deleting topic '$topic'" }
    deleteTopic(topic)
}

fun PubSubAdmin.hasSubscription(subscription: String) = listSubscriptions().any { it.simpleName() == subscription }

fun PubSubAdmin.createSubscriptionIfNotExists(topic: String, subscription: String) {
    if (hasSubscription(subscription)) return
    log.info { "Creating subscription '${subscription}'" }
    createSubscription(subscription, topic)
}

fun PubSubAdmin.deleteSubscriptionIfExists(subscription: String) {
    if (hasSubscription(subscription)) {
        log.info { "Deleting subscription '$subscription'" }
        deleteSubscription(subscription)
    }
}
