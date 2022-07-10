package softwarebrewery.demo.domain.model

import softwarebrewery.demo.domain.*
import softwarebrewery.demo.domain.ports.*

class DomainHandler(
    private val offerRepository: OfferRepository,
    private val promoRepository: PromoRepository,
    private val offerPromoLinker: OfferPromoLinker,
) : DomainApi {

    override fun handle(event: OfferCreated) {
        val offer = offerRepository.new(
            offerId = event.offerId,
            productId = event.productId,
            country = event.country,
        )
        offerRepository.insert(offer).let { persisted ->
            offerPromoLinker.linkPromosToOffer(persisted.it)
        }
    }

    override fun handle(event: OfferDeleted): Unit = throw UnsupportedOperationException()

    override fun handle(event: PromotionActivated) {
        val promo = promoRepository.new(
            promotionId = event.promotionId,
            productId = event.productId,
            country = event.country,
        )
        promoRepository.insert(promo).let { persisted ->
            offerPromoLinker.linkOffersToPromo(persisted.it)
        }
    }

    override fun handle(event: PromotionDeactivated): Unit = throw UnsupportedOperationException()

}
