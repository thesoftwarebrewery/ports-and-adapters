package softwarebrewery.demo.domain.api

import softwarebrewery.demo.domain.*
import softwarebrewery.demo.domain.model.*
import java.time.*

@DomainEvent
data class PromotionDeactivated(
    val publishedAt: Instant,
    val promotionId: PromotionId,
)
