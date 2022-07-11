package softwarebrewery.app.adapters.application

import org.springframework.context.*
import org.springframework.transaction.annotation.*
import org.springframework.transaction.annotation.Propagation.*
import org.springframework.transaction.event.*
import org.springframework.transaction.event.TransactionPhase.*
import softwarebrewery.app.adapters.application.LinkTrigger.*
import softwarebrewery.app.domain.*
import softwarebrewery.app.domain.model.*
import softwarebrewery.app.domain.ports.*

@DrivenAdapter  // activated by domain logic to fulfill OfferPromoLinker role
@DrivingAdapter // activates domain logic of OfferPromoLinker
@Transactional
class TransactionAwareOfferPromoLinker(
    private val linkRequestRepo: JdbcLinkRequestRepo,
    private val eventPublisher: ApplicationEventPublisher,
    private val offerPromoLinker: OfferPromoLinker,
) : OfferPromoLinker {

    override fun linkPromosToOffer(offer: Offer) {
        linkRequestRepo.insert(OFFER, offer.offerId)
        eventPublisher.publishEvent(LinkingRequested(offer))
    }

    override fun linkOffersToPromo(promo: Promo) {
        linkRequestRepo.insert(PROMO, promo.promotionId)
        eventPublisher.publishEvent(LinkingRequested(promo))
    }

    @Transactional(propagation = REQUIRES_NEW)
    @TransactionalEventListener(phase = AFTER_COMMIT)
    fun handle(linkingRequested: LinkingRequested<*>) {
        when (val trigger = linkingRequested.trigger) {
            is Offer -> {
                offerPromoLinker.linkPromosToOffer(trigger)
                linkRequestRepo.remove(OFFER, trigger.offerId)
            }
            is Promo -> {
                offerPromoLinker.linkOffersToPromo(trigger)
                linkRequestRepo.remove(PROMO, trigger.promotionId)
            }
        }
    }

    class LinkingRequested<T>(val trigger: T)

}
