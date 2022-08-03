package softwarebrewery.app.adapters.application

import softwarebrewery.app.domain.ports.*
import org.springframework.context.annotation.*
import org.springframework.jdbc.core.namedparam.*
import java.time.*

@Configuration
class ApplicationAdapterConfig {

    @Bean
    fun offerRepo(db: NamedParameterJdbcTemplate, clock: () -> Instant): OfferRepo =
        JdbcOfferRepo(db, clock)

    @Bean
    fun promoRepo(db: NamedParameterJdbcTemplate, clock: () -> Instant): PromoRepo =
        JdbcPromoRepo(db, clock)

}
