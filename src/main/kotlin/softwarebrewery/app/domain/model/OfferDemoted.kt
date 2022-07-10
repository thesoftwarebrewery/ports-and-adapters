package softwarebrewery.app.domain.model

import softwarebrewery.app.domain.*
import java.time.*

@DomainEvent
data class OfferDemoted(
    val publishedAt: Instant,
    val offerId: OfferId,
    val promotionId: PromotionId,
)
