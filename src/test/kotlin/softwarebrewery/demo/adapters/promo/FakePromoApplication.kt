package softwarebrewery.demo.adapters.promo

import com.google.cloud.spring.pubsub.core.*
import org.springframework.beans.factory.annotation.*
import org.springframework.stereotype.*
import softwarebrewery.demo.infra.*

@Component
class FakePromoApplication(
    @Value("\${pubsub.promo.promo-events.topic}") val promotionEventsTopic: String,
    @Value("\${pubsub.promo.promo-events.subscription}") val promotionEventsSubscription: String,
    private val pubSub: PubSubOperations,
) {

    fun createPromotion(promotionId: String, productId: String, country: String) {
        val message = aPromotionMessage(promotionId = promotionId, productId = productId, country = country)
        val promotionCreated = aPubSubMessage(
            attributes = mapOf("change_type" to "CREATE"),
            data = message.toJson(),
        )
        pubSub.publishSync(promotionEventsTopic, promotionCreated)
    }
}
