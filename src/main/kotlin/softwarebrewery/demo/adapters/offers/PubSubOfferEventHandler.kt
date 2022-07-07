package softwarebrewery.demo.adapters.offers

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

        when (val eventType = attributes[MessageAttributes.EVENT_TYPE]) {
            "OFFER_CREATED" -> domainApi.handle(offerCreated.toDomainOfferCreated())
            else -> throw UnsupportedOperationException(eventType)
        }
    }
}
