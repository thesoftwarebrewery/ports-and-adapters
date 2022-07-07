package softwarebrewery.demo.adapters.offers

import com.google.cloud.spring.pubsub.core.*
import org.springframework.beans.factory.annotation.*
import org.springframework.stereotype.*
import softwarebrewery.demo.technical.*

@Component
class FakeOffers(
    @Value("\${pubsub.offer.offer-events.topic}") val offerEventsTopic: String,
    @Value("\${pubsub.offer.offer-events.subscription}") val offerEventsSubscription: String,
    private val pubSub: PubSubOperations,
) {

    fun publish(message: ExternalOfferCreated) =
        pubSub.publishSync(offerEventsTopic, aPubSubMessage(data = message.toJson()))

}
