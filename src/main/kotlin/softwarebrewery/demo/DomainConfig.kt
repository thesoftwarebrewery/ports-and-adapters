package softwarebrewery.demo

import org.springframework.context.annotation.*
import softwarebrewery.demo.domain.api.*
import softwarebrewery.demo.domain.model.*
import softwarebrewery.demo.domain.ports.*
import softwarebrewery.demo.domain.ports.Clock
import java.time.*

@Configuration
class DomainConfig {

    @Bean
    fun clock(systemClock: () -> Instant) = Clock { systemClock() }

    @Bean
    fun offerRepository(clock: () -> Instant) = InMemOfferRepository(clock)

    @Bean
    fun promotionRepository(clock: () -> Instant) = InMemPromotionRepository(clock)

    @Bean
    fun offerPromotionListener() = InMemOfferPromoListener()

    @Bean
    fun domainApi(
        offerRepository: OfferRepository,
        promotionRepository: PromotionRepository,
        offerPromotionListener: OfferPromotionListener,
        clock: Clock,
    ): DomainApi = DomainHandler(
        offerRepository = offerRepository,
        promotionRepository = promotionRepository,
        offerPromotionListener = offerPromotionListener,
        clock = clock,
    )
}
