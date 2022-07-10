package softwarebrewery.app

import org.springframework.beans.factory.annotation.*
import org.springframework.context.annotation.*
import softwarebrewery.app.adapters.promo.*
import softwarebrewery.app.domain.*
import softwarebrewery.app.infra.pubsub.*

@Configuration
class PromoAdapterConfig {

    @Bean
    fun promoMessageSubscriber(
        @Value("\${pubsub.promo.promo-events.subscription}") subscription: String,
        subscribe: NewPubSubSubscriber,
        domainApi: DomainApi,
    ) = subscribe(subscription, PubSubPromoMessageHandler(domainApi))
}
