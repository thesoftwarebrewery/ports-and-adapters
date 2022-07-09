package softwarebrewery.demo

import org.springframework.beans.factory.annotation.*
import org.springframework.context.annotation.*
import softwarebrewery.demo.adapters.offer.*
import softwarebrewery.demo.domain.*
import softwarebrewery.demo.domain.ports.*
import softwarebrewery.demo.infra.pubsub.*

@Configuration
class OfferAdapterConfig {

    @Bean
    fun pubSubOfferEventSubscriber(
        @Value("\${pubsub.offer.offer-events.subscription}") subscription: String,
        subscribe: NewPubSubSubscriber,
        domainApi: DomainApi,
    ) = subscribe(subscription, PubSubOfferEventSubscriber(domainApi))

    @Bean
    fun offerPromoEventPublisher(
        @Value("\${pubsub.offer.offer-promo-events.topic}") topic: String,
        pubSubOutbox: PubSubOutbox,
    ): OfferPromoListener = PubSubOfferPromoEventPublisher(
        topic = topic,
        pubSubOutbox = pubSubOutbox,
    )
}
