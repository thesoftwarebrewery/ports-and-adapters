package softwarebrewery.demo.domain.model

import softwarebrewery.demo.domain.ports.*

class OfferPromotionLinker(
    private val offerRepository: OfferRepository,
    private val promotionRepository: PromotionRepository,
    private val offerPromotionListener: OfferPromotionListener,
    private val clock: Clock,
) {

    fun linkOffersToPromo(promo: Promotion) {
        val appliedAt = clock()
        offerRepository.findByProductId(promo.productId)
            .mapNotNull {
                if (!promo.appliesTo(it)) null
                else OfferPromoted(
                    publishedAt = appliedAt,
                    offerId = it.offerId,
                    promotionId = promo.promotionId,
                )
            }.forEach { offerPromotionListener.handle(it) }
    }

    fun linkPromosToOffer(offer: Offer) {
        val appliedAt = clock()
        promotionRepository.findByProductId(offer.productId)
            .mapNotNull {
                if (!it.appliesTo(offer)) null
                else OfferPromoted(
                    publishedAt = appliedAt,
                    offerId = offer.offerId,
                    promotionId = it.promotionId,
                )
            }.forEach { offerPromotionListener.handle(it) }
    }
}
