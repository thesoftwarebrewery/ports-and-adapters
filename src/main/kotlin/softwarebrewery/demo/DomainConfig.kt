package softwarebrewery.demo

import org.springframework.context.*
import org.springframework.context.annotation.*
import org.springframework.jdbc.core.namedparam.*
import org.springframework.transaction.annotation.*
import softwarebrewery.demo.adapters.application.*
import softwarebrewery.demo.domain.*
import softwarebrewery.demo.domain.model.*
import softwarebrewery.demo.domain.ports.*
import softwarebrewery.demo.domain.ports.Clock
import java.time.*

@Configuration
class DomainConfig {

    @Bean
    fun clock(systemClock: () -> Instant) = Clock { systemClock() }

    @Bean
    fun domainApi(
        offerRepository: OfferRepository,
        promoRepository: PromoRepository,
        offerPromoListener: OfferPromoListener,
        offerPromoLinker: OfferPromoLinker,
    ): DomainApi {
        return TransactionalDomainHandler(
            domainApi = DomainHandler(
                offerRepository = offerRepository,
                promoRepository = promoRepository,
                offerPromoLinker = offerPromoLinker,
            )
        )
    }

    @Bean
    fun offerPromoLinker(
        db: NamedParameterJdbcTemplate,
        offerRepository: OfferRepository,
        promoRepository: PromoRepository,
        offerPromoListener: OfferPromoListener,
        clock: Clock,
        eventPublisher: ApplicationEventPublisher,
    ) = TransactionAwareOfferPromoLinker(
        linkRequestRepo = JdbcLinkRequestRepo(db),
        eventPublisher = eventPublisher,
        offerPromoLinker = DirectOfferPromoLinker(
            offerRepository = offerRepository,
            promoRepository = promoRepository,
            offerPromoListener = offerPromoListener,
            clock = clock,
        ),
    )

    @Transactional
    class TransactionalDomainHandler(domainApi: DomainApi) : DomainApi by domainApi

}
