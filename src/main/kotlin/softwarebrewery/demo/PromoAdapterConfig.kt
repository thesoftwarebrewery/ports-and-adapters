package softwarebrewery.demo

import org.springframework.beans.factory.annotation.*
import org.springframework.context.annotation.*
import softwarebrewery.demo.adapters.promo.*
import softwarebrewery.demo.domain.api.*
import softwarebrewery.demo.infra.pubsub.*

@Configuration
class PromoAdapterConfig {

    @Bean
    fun promoMessageSubscriber(
        @Value("\${pubsub.promo.promo-events.subscription}") subscription: String,
        subscribe: NewPubSubSubscriber,
        domainApi: DomainApi,
    ) = subscribe(subscription, PubSubPromoMessageHandler(domainApi))
}
