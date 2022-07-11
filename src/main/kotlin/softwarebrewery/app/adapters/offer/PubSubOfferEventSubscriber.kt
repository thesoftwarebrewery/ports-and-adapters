package softwarebrewery.app.adapters.offer

import softwarebrewery.app.adapters.offer.MessageAttributes.Companion.EVENT_TYPE
import softwarebrewery.app.adapters.offer.MessageAttributes.Companion.EVENT_TYPE_OFFER_CREATED
import softwarebrewery.app.domain.*
import softwarebrewery.infra.pubsub.*
import java.nio.*

@DrivingAdapter
class PubSubOfferEventSubscriber(
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
