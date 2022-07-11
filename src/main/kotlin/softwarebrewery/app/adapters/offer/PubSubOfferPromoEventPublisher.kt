package softwarebrewery.app.adapters.offer

import org.springframework.beans.factory.annotation.*
import softwarebrewery.app.adapters.offer.MessageAttributes.Companion.EVENT_TYPE
import softwarebrewery.app.adapters.offer.MessageAttributes.Companion.EVENT_TYPE_OFFER_PROMOTED
import softwarebrewery.app.domain.*
import softwarebrewery.app.domain.model.*
import softwarebrewery.app.domain.ports.*
import softwarebrewery.infra.*
import softwarebrewery.infra.pubsub.*
import java.nio.*

@DrivenAdapter
class PubSubOfferPromoEventPublisher(
    @Value("\${pubsub.offer.offer-promo-events.topic}") private val topic: String,
    private val pubSubOutbox: PubSubOutbox,
) : OfferPromoAnnouncer {

    override fun announce(event: OfferPromoted) {
        val externalOfferPromoted = ExternalOfferPromoted(
            publishedAt = event.publishedAt,
            promotionId = event.promotionId,
            country = event.country,
            offerId = event.offerId,
        )
        val message = Message(
            id = randomUuid(),
            topic = topic,
            attributes = mapOf(EVENT_TYPE to EVENT_TYPE_OFFER_PROMOTED),
            data = ByteBuffer.wrap(externalOfferPromoted.toJson().toByteArray()),
        )
        pubSubOutbox.post(message)
    }

    override fun announce(event: OfferDemoted): Unit = throw UnsupportedOperationException()

}
