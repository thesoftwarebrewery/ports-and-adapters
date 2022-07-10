package softwarebrewery.demo.adapters.offer

import org.springframework.beans.factory.annotation.*
import softwarebrewery.demo.adapters.offer.MessageAttributes.Companion.EVENT_TYPE
import softwarebrewery.demo.adapters.offer.MessageAttributes.Companion.EVENT_TYPE_OFFER_PROMOTED
import softwarebrewery.demo.domain.*
import softwarebrewery.demo.domain.model.*
import softwarebrewery.demo.domain.ports.*
import softwarebrewery.demo.infra.pubsub.*
import java.nio.*
import java.util.UUID.*

@DrivenAdapter
class PubSubOfferPromoEventPublisher(
    @Value("\${pubsub.offer.offer-promo-events.topic}") private val topic: String,
    private val pubSubOutbox: PubSubOutbox,
) : OfferPromoListener {

    override fun handle(event: OfferPromoted) {
        val externalOfferPromotedEvent = ExternalOfferPromoted(
            publishedAt = event.publishedAt,
            promotionId = event.promotionId,
            country = event.country,
            offerId = event.offerId,
        )
        pubSubOutbox.post(
            Message(
                id = randomUUID().toString(),
                topic = topic,
                attributes = mapOf(EVENT_TYPE to EVENT_TYPE_OFFER_PROMOTED),
                data = ByteBuffer.wrap(externalOfferPromotedEvent.toJson().toByteArray()),
            )
        )
    }

    override fun handle(event: OfferDemoted) {
        throw UnsupportedOperationException()
    }
}
