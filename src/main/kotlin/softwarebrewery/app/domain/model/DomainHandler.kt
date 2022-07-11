package softwarebrewery.app.domain.model

import softwarebrewery.app.domain.*
import softwarebrewery.app.domain.ports.*

class DomainHandler(
    private val offerRepo: OfferRepo,
    private val promoRepo: PromoRepo,
    private val offerPromoLinker: OfferPromoLinker,
) : DomainApi {

    override fun handle(event: OfferCreated) {
        val offer = offerRepo.new(
            offerId = event.offerId,
            productId = event.productId,
            country = event.country,
        )
        offerRepo.insert(offer).let { persisted ->
            offerPromoLinker.linkPromosToOffer(offer = persisted.it)
        }
    }

    override fun handle(event: OfferDeleted): Unit = throw UnsupportedOperationException()

    override fun handle(event: PromotionActivated) {
        val promo = promoRepo.new(
            promotionId = event.promotionId,
            productId = event.productId,
            country = event.country,
        )
        promoRepo.insert(promo).let { persisted ->
            offerPromoLinker.linkOffersToPromo(promo = persisted.it)
        }
    }

    override fun handle(event: PromotionDeactivated): Unit = throw UnsupportedOperationException()

}
