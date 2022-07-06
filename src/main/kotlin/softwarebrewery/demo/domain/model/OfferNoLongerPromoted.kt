package softwarebrewery.demo.domain.model

import softwarebrewery.demo.domain.OfferId
import softwarebrewery.demo.domain.PromotionId
import java.time.Instant

data class OfferNoLongerPromoted(
    val publishedAt: Instant,
    val offerId: OfferId,
    val promotionId: PromotionId,
)
