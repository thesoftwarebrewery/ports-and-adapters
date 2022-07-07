package softwarebrewery.demo.adapters.promo

import org.springframework.beans.factory.annotation.*
import org.springframework.context.annotation.*
import softwarebrewery.demo.domain.api.*
import softwarebrewery.demo.infra.pubsub.*

@Configuration
class PromoAdapterConfig {

    @Bean
    fun promoMessageSubscriber(
        @Value("\${pubsub.promo.promotion-events.subscription}") subscription: String,
        factory: PubSubSubscriber.Factory,
        domainApi: DomainApi,
    ) = factory.newSubscriber(
        subscription = subscription,
        handler = PubSubPromotionMessageHandler(domainApi),
    )
}
