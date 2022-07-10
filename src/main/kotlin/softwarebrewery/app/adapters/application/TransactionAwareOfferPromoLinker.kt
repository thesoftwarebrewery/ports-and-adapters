package softwarebrewery.app.adapters.application

import org.springframework.context.*
import org.springframework.transaction.annotation.*
import org.springframework.transaction.annotation.Propagation.*
import org.springframework.transaction.event.*
import org.springframework.transaction.event.TransactionPhase.*
import softwarebrewery.app.adapters.application.JdbcLinkRequestRepo.*
import softwarebrewery.app.domain.model.*
import softwarebrewery.app.domain.ports.*

@Transactional
class TransactionAwareOfferPromoLinker(
    private val linkRequestRepo: JdbcLinkRequestRepo,
    private val eventPublisher: ApplicationEventPublisher,
    private val offerPromoLinker: OfferPromoLinker,
) : OfferPromoLinker {

    override fun linkPromosToOffer(offer: Offer) {
        linkRequestRepo.insert(LinkTrigger.Offer, offer.offerId)
        eventPublisher.publishEvent(LinkingRequested(offer))
    }

    override fun linkOffersToPromo(promo: Promo) {
        linkRequestRepo.insert(LinkTrigger.Promo, promo.promotionId)
        eventPublisher.publishEvent(LinkingRequested(promo))
    }

    @Transactional(propagation = REQUIRES_NEW)
    @TransactionalEventListener(phase = AFTER_COMMIT)
    fun handle(linkingRequested: LinkingRequested<*>) {
        when (val trigger = linkingRequested.trigger) {
            is Offer -> {
                offerPromoLinker.linkPromosToOffer(trigger)
                linkRequestRepo.remove(LinkTrigger.Offer, trigger.offerId)
            }
            is Promo -> {
                offerPromoLinker.linkOffersToPromo(trigger)
                linkRequestRepo.remove(LinkTrigger.Promo, trigger.promotionId)
            }
        }
    }

    class LinkingRequested<T>(val trigger: T)

}
