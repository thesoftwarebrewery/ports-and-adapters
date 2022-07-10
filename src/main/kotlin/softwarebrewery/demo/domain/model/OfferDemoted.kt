package softwarebrewery.demo.domain.model

import softwarebrewery.demo.domain.*
import java.time.*

@DomainEvent
data class OfferDemoted(
    val publishedAt: Instant,
    val offerId: OfferId,
    val promotionId: PromotionId,
)
