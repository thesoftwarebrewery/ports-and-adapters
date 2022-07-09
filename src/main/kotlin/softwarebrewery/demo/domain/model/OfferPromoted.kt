package softwarebrewery.demo.domain.model

import softwarebrewery.demo.domain.*
import java.time.*

@DomainEvent
data class OfferPromoted(
    val publishedAt: Instant,
    val offerId: OfferId,
    val country: Country,
    val promotionId: PromotionId,
)
