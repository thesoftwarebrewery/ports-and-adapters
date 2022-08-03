package softwarebrewery.app

import softwarebrewery.app.adapters.promo.*
import softwarebrewery.app.domain.*
import softwarebrewery.infra.pubsub.*
import org.springframework.beans.factory.annotation.*
import org.springframework.context.annotation.*

@Configuration
class PromoAdapterConfig {

    @Bean
    fun promoMessageSubscriber(
        @Value("\${pubsub.promo.promo-events.subscription}") subscription: String,
        subscribe: NewPubSubSubscriber,
        domainApi: DomainApi,
    ): PubSubSubscriber = subscribe(
        subscription = subscription,
        handler = PubSubPromoMessageHandler(domainApi),
    )
}
