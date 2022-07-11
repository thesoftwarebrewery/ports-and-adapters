package softwarebrewery.app.adapters.offer

import com.google.cloud.spring.pubsub.core.*
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.*
import org.springframework.stereotype.*
import softwarebrewery.app.infra.json.*
import softwarebrewery.app.infra.pubsub.*
import java.time.Instant.*

@Component
class FakeOfferDomain(
    @Value("\${pubsub.offer.offer-events.topic}") val offerEventsTopic: String,
    @Value("\${pubsub.offer.offer-promo-events.subscription}") val offerPromotionsTopic: String,
    private val pubSub: PubSubOperations,
) {

    fun publish(message: ExternalOfferCreated) {
        val pubSubMessage = aPubSubMessage(
            attributes = mapOf("event_type" to "OFFER_CREATED"),
            data = message.toJson(),
        )
        pubSub.publishSync(offerEventsTopic, pubSubMessage)
    }

    fun hasReceived(message: ExternalOfferPromoted) =
        pubSub.assertReceived(offerPromotionsTopic) {
            assertThat(it.attributesMap["event_type"]).isEqualTo("OFFER_PROMOTED")

            val actual = assertDoesNotThrow { it.data.deserializeJsonAs<ExternalOfferPromoted>() }

            assertThat(actual.offerId).isEqualTo(message.offerId)
            assertThat(actual.country).isEqualTo(message.country)
            assertThat(actual.promotionId).isEqualTo(message.promotionId)
            // for now a shade of 'recentness' will suffice
            assertThat(actual.publishedAt).isBetween(now().minusSeconds(5), now())
        }
}
