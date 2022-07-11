package softwarebrewery.app.adapters.promo

import com.google.cloud.spring.pubsub.core.*
import org.springframework.beans.factory.annotation.*
import org.springframework.stereotype.*
import softwarebrewery.infra.pubsub.*

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
