package softwarebrewery.demo.adapters.offer

import com.google.cloud.spring.pubsub.core.*
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.*
import org.springframework.stereotype.*
import softwarebrewery.demo.domain.model.*
import softwarebrewery.demo.infra.*
import java.time.*

@Component
class FakeOfferApplication(
    @Value("\${pubsub.offer.offer-events.topic}") val offerEventsTopic: String,
    @Value("\${pubsub.offer.offer-promo-events.subscription}") val offerPromoEventsSubscription: String,
    private val pubSub: PubSubOperations,
) {

    fun createOffer(offerId: String, productId: String, country: String) {
        val offerCreated = anExternalOfferCreated(offerId = offerId, productId = productId, country = country)

        pubSub.publishSync(
            offerEventsTopic, aPubSubMessage(
                attributes = mapOf("event_type" to "OFFER_CREATED"),
                data = offerCreated.toJson(),
            )
        )
    }

    fun hasReceived(expected: OfferPromoted) {
        pubSub.assertReceived(offerPromoEventsSubscription) {
            Assertions.assertThat(it.attributesMap["event_type"]).isEqualTo("OFFER_PROMOTED")

            val actual = assertDoesNotThrow { it.data.deserializeJsonAs<OfferPromoted>() }

            Assertions.assertThat(actual.offerId).isEqualTo(expected.offerId)
            Assertions.assertThat(actual.country).isEqualTo(expected.country)
            Assertions.assertThat(actual.promotionId).isEqualTo(expected.promotionId)
            Assertions.assertThat(actual.publishedAt).isBetween(Instant.now().minusSeconds(5), Instant.now())
        }
    }
}
