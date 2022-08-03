package softwarebrewery.app

import softwarebrewery.app.adapters.application.*
import softwarebrewery.app.domain.*
import softwarebrewery.app.domain.model.*
import softwarebrewery.app.domain.ports.*
import softwarebrewery.app.domain.ports.Clock
import org.springframework.context.*
import org.springframework.context.annotation.*
import org.springframework.jdbc.core.namedparam.*
import java.time.*

@Configuration
class DomainConfig {

    @Bean
    fun clock(systemClock: () -> Instant) = Clock { systemClock() }

    @Bean
    fun domainApi(
        offerRepo: OfferRepo,
        promoRepo: PromoRepo,
        offerPromoAnnouncer: OfferPromoAnnouncer,
        offerPromoLinker: OfferPromoLinker,
    ): DomainApi = TransactionalDomainApi(
        domainApi = DomainHandler(
            offerRepo = offerRepo,
            promoRepo = promoRepo,
            offerPromoLinker = offerPromoLinker,
        )
    )

    @Bean
    fun offerPromoLinker(
        db: NamedParameterJdbcTemplate,
        offerRepo: OfferRepo,
        promoRepo: PromoRepo,
        offerPromoAnnouncer: OfferPromoAnnouncer,
        clock: Clock,
        eventPublisher: ApplicationEventPublisher,
    ) = TransactionAwareOfferPromoLinker(
        linkRequestRepo = JdbcLinkRequestRepo(db),
        eventPublisher = eventPublisher,
        offerPromoLinker = DirectOfferPromoLinker(
            offerRepo = offerRepo,
            promoRepo = promoRepo,
            offerPromoAnnouncer = offerPromoAnnouncer,
            clock = clock,
        ),
    )
}
