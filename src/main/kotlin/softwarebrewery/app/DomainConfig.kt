package softwarebrewery.app

import org.springframework.context.*
import org.springframework.context.annotation.*
import org.springframework.jdbc.core.namedparam.*
import org.springframework.transaction.annotation.*
import softwarebrewery.app.adapters.application.*
import softwarebrewery.app.domain.*
import softwarebrewery.app.domain.model.*
import softwarebrewery.app.domain.ports.*
import softwarebrewery.app.domain.ports.Clock
import java.time.*

@Configuration
class DomainConfig {

    @Bean
    fun clock(systemClock: () -> Instant) = Clock { systemClock() }

    @Bean
    fun domainApi(
        offerRepo: OfferRepo,
        promoRepo: PromoRepo,
        offerPromoListener: OfferPromoListener,
        offerPromoLinker: OfferPromoLinker,
    ): DomainApi {
        return TransactionalDomainHandler(
            domainApi = DomainHandler(
                offerRepo = offerRepo,
                promoRepo = promoRepo,
                offerPromoLinker = offerPromoLinker,
            )
        )
    }

    @Bean
    fun offerPromoLinker(
        db: NamedParameterJdbcTemplate,
        offerRepo: OfferRepo,
        promoRepo: PromoRepo,
        offerPromoListener: OfferPromoListener,
        clock: Clock,
        eventPublisher: ApplicationEventPublisher,
    ) = TransactionAwareOfferPromoLinker(
        linkRequestRepo = JdbcLinkRequestRepo(db),
        eventPublisher = eventPublisher,
        offerPromoLinker = DirectOfferPromoLinker(
            offerRepo = offerRepo,
            promoRepo = promoRepo,
            offerPromoListener = offerPromoListener,
            clock = clock,
        ),
    )

    @Transactional
    class TransactionalDomainHandler(domainApi: DomainApi) : DomainApi by domainApi

}
