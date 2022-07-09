package softwarebrewery.demo

import org.springframework.context.annotation.*
import softwarebrewery.demo.adapters.application.*
import java.time.*

@Configuration
class ApplicationAdapterConfig {

    @Bean
    fun offerRepository(clock: () -> Instant) = InMemOfferRepository(clock)

    @Bean
    fun promotionRepository(clock: () -> Instant) = InMemPromoRepository(clock)

}
