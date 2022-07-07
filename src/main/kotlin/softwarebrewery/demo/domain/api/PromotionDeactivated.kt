package softwarebrewery.demo.domain.api

import softwarebrewery.demo.domain.model.*
import java.time.*

data class PromotionDeactivated(
    val publishedAt: Instant,
    val promotionId: PromotionId,
)
