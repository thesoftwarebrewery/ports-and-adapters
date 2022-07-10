package softwarebrewery.app

import org.springframework.context.annotation.*
import org.springframework.jdbc.core.namedparam.*
import softwarebrewery.app.adapters.application.*
import java.time.*

@Configuration
class ApplicationAdapterConfig {

    @Bean
    fun offerRepository(db: NamedParameterJdbcTemplate, clock: () -> Instant) =
        JdbcOfferRepo(db, clock)

    @Bean
    fun promotionRepository(db: NamedParameterJdbcTemplate, clock: () -> Instant) =
        JdbcPromoRepo(db, clock)

}
