package softwarebrewery.demo.domain.ports

import softwarebrewery.demo.domain.OfferId
import softwarebrewery.demo.domain.PromotionId
import java.time.Instant

interface OfferPromotionListener {
    fun handle(offerPromoted: OfferPromoted)
    fun handle(offerDemoted: OfferDemoted)
}

data class OfferPromoted(
    val publishedAt: Instant,
    val offerId: OfferId,
    val promotionId: PromotionId,
)

data class OfferDemoted(
    val publishedAt: Instant,
    val offerId: OfferId,
    val promotionId: PromotionId,
)
