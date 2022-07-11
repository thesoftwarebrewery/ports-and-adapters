package softwarebrewery.app

import org.springframework.beans.factory.annotation.*
import org.springframework.context.annotation.*
import softwarebrewery.app.adapters.offer.*
import softwarebrewery.app.domain.*
import softwarebrewery.app.domain.ports.*
import softwarebrewery.app.infra.pubsub.*

@Configuration
class OfferAdapterConfig {

    @Bean
    fun pubSubOfferEventSubscriber(
        @Value("\${pubsub.offer.offer-events.subscription}") subscription: String,
        subscribe: NewPubSubSubscriber,
        domainApi: DomainApi,
    ): PubSubSubscriber = subscribe(
        subscription = subscription,
        handler = PubSubOfferEventSubscriber(domainApi),
    )

    @Bean
    fun offerPromoEventPublisher(
        @Value("\${pubsub.offer.offer-promo-events.topic}") topic: String,
        pubSubOutbox: PubSubOutbox,
    ): OfferPromoAnnouncer = PubSubOfferPromoEventPublisher(
        topic = topic,
        pubSubOutbox = pubSubOutbox,
    )
}
