package softwarebrewery.demo.infra.pubsub

import com.google.cloud.spring.pubsub.*
import org.springframework.beans.factory.annotation.*
import org.springframework.boot.test.context.*
import javax.annotation.*

@TestConfiguration
class PubSubTestConfig(
    private val pubSubAdmin: PubSubAdmin,
    @Value("\${pubsub.promo.promo-events.topic}") private val promoTopic: String,
    @Value("\${pubsub.promo.promo-events.subscription}") private val promoSubscription: String,
    @Value("\${pubsub.offer.offer-events.topic}") private val offerTopic: String,
    @Value("\${pubsub.offer.offer-events.subscription}") private val offerSubscription: String,
    @Value("\${pubsub.offer.offer-promo-events.topic}") private val offerPromoTopic: String,
    @Value("\${pubsub.offer.offer-promo-events.subscription}") private val offerPromoSubscription: String,
) {

    private val subscriptions = setOf(
        promoTopic to promoSubscription,
        offerTopic to offerSubscription,
        offerPromoTopic to offerPromoSubscription,
    )

    @PostConstruct
    fun recreateTopicSubscriptions() {
        subscriptions.reversed().forEach { (_, subscription) -> pubSubAdmin.deleteSubscriptionIfExists(subscription) }
        subscriptions.reversed().forEach { (topic, _) -> pubSubAdmin.deleteTopicIfExists(topic) }
        subscriptions.forEach { (topic, subscription) ->
            pubSubAdmin.createTopicIfNotExists(topic)
            pubSubAdmin.createSubscriptionIfNotExists(topic = topic, subscription = subscription)
        }
    }
}
