package softwarebrewery.demo.domain.model

import softwarebrewery.demo.domain.ports.*
import softwarebrewery.demo.domain.ports.Clock
import java.time.*

class OfferPromotionLinker(
    private val offerRepository: OfferRepository,
    private val promoRepository: PromoRepository,
    private val offerPromoListener: OfferPromoListener,
    private val clock: Clock,
) {

    fun linkOffersToPromo(promo: Promo) {
        val appliedAt = clock()
        offerRepository.findByProductId(promo.productId)
            .mapNotNull { offer -> offerPromotedIfApplicable(promo, offer, appliedAt) }
            .forEach { offerPromoListener.handle(it) }
    }

    fun linkPromosToOffer(offer: Offer) {
        val appliedAt = clock()
        promoRepository.findByProductId(offer.productId)
            .mapNotNull { promo -> offerPromotedIfApplicable(promo, offer, appliedAt) }
            .forEach { offerPromoListener.handle(it) }
    }

    private fun offerPromotedIfApplicable(promo: Promo, offer: Offer, appliedAt: Instant): OfferPromoted? =
        if (!promo.appliesTo(offer)) null
        else OfferPromoted(
            publishedAt = appliedAt,
            offerId = offer.offerId,
            promotionId = promo.promotionId,
            country = offer.country,
        )
}
