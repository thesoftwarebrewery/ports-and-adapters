package softwarebrewery.demo.adapters.application

import org.springframework.context.annotation.*
import java.time.*

@Configuration
class ApplicationAdapterConfig {

    @Bean
    fun offerRepository(clock: () -> Instant) = InMemOfferRepository(clock)

    @Bean
    fun promotionRepository(clock: () -> Instant) = InMemPromotionRepository(clock)

    @Bean
    fun offerPromotionListener() = InMemOfferPromoListener()

}
