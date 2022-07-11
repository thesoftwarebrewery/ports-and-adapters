package softwarebrewery.app.domain.model

import softwarebrewery.app.domain.ports.*
import softwarebrewery.app.domain.ports.Clock
import java.time.*

class DirectOfferPromoLinker(
    private val offerRepo: OfferRepo,
    private val promoRepo: PromoRepo,
    private val offerPromoAnnouncer: OfferPromoAnnouncer,
    private val clock: Clock,
) : OfferPromoLinker {

    override fun linkOffersToPromo(promo: Promo) {
        val appliedAt = clock()
        offerRepo.findByProductId(promo.productId)
            .mapNotNull { offer -> offerPromotedIfApplicable(promo, offer, appliedAt) }
            .forEach { offerPromoAnnouncer.announce(it) }
    }

    override fun linkPromosToOffer(offer: Offer) {
        val appliedAt = clock()
        promoRepo.findByProductId(offer.productId)
            .mapNotNull { promo -> offerPromotedIfApplicable(promo, offer, appliedAt) }
            .forEach { offerPromoAnnouncer.announce(it) }
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
