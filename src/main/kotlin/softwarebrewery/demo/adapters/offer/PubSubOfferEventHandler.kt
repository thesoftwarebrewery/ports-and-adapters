package softwarebrewery.demo.adapters.offer

import softwarebrewery.demo.adapters.offer.MessageAttributes.Companion.EVENT_TYPE
import softwarebrewery.demo.adapters.offer.MessageAttributes.Companion.EVENT_TYPE_OFFER_CREATED
import softwarebrewery.demo.domain.*
import softwarebrewery.demo.domain.api.*
import softwarebrewery.demo.infra.pubsub.*
import java.nio.*

@DrivingAdapter
class PubSubOfferEventHandler(
    private val domainApi: DomainApi,
) : MessageHandler {

    override fun invoke(attributes: Map<String, String>, data: ByteBuffer) {
        val offerCreated = ExternalOfferCreated.fromJsonBytes(data)

        when (val eventType = attributes[EVENT_TYPE]) {
            EVENT_TYPE_OFFER_CREATED -> domainApi.handle(offerCreated.toDomainOfferCreated())
            else -> throw UnsupportedOperationException(eventType)
        }
    }
}
