package softwarebrewery.demo.domain.api

import softwarebrewery.demo.domain.model.*
import java.time.*

data class PromotionActivated(
    val publishedAt: Instant,
    val promotionId: PromotionId,
    val productId: ProductId,
    val country: Country
)
