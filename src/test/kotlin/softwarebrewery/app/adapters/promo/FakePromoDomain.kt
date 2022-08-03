package softwarebrewery.app.adapters.promo

import softwarebrewery.infra.pubsub.*
import com.google.cloud.spring.pubsub.core.*
import org.springframework.beans.factory.annotation.*
import org.springframework.stereotype.*

@Component
class FakePromoDomain(
    @Value("\${pubsub.promo.promo-events.topic}") val promotionEventsTopic: String,
    private val pubSub: PubSubOperations,
) {

    fun publish(message: ExternalPromotionMessage) {
        val pubSubMessage = aPubSubMessage(
            attributes = mapOf("change_type" to "CREATE"),
            data = message.toJson(),
        )
        pubSub.publishSync(promotionEventsTopic, pubSubMessage)
    }
}
