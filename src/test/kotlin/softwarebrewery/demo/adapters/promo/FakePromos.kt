package softwarebrewery.demo.adapters.promo

import com.google.cloud.spring.pubsub.core.*
import org.springframework.beans.factory.annotation.*
import org.springframework.stereotype.*
import softwarebrewery.demo.technical.*

@Component
class FakePromos(
    @Value("\${pubsub.promo.promotion-events.topic}") val promotionEventsTopic: String,
    @Value("\${pubsub.promo.promotion-events.subscription}") val promotionEventsSubscription: String,
    private val pubSub: PubSubOperations,
) {

    fun publish(message: PromotionMessage) =
        pubSub.publishSync(promotionEventsTopic, aPubSubMessage(data = message.toJson()))

}
