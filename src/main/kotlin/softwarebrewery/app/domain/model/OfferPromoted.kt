package softwarebrewery.app.domain.model

import softwarebrewery.app.domain.*
import java.time.*

@DomainEvent
data class OfferPromoted(
    val publishedAt: Instant,
    val offerId: OfferId,
    val country: Country,
    val promotionId: PromotionId,
)
