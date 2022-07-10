package softwarebrewery.demo

import org.springframework.context.annotation.*
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import softwarebrewery.demo.adapters.application.*
import java.time.*

@Configuration
class ApplicationAdapterConfig {

    @Bean
    fun offerRepository(db: NamedParameterJdbcTemplate, clock: () -> Instant) =
        JdbcOfferRepository(db, clock)

    @Bean
    fun promotionRepository(clock: () -> Instant) = InMemPromoRepository(clock)

}
