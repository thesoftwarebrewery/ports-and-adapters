package softwarebrewery.demo.domain.api

import softwarebrewery.demo.domain.model.*
import java.time.*

sealed class PromotionEvent

data class PromotionActivated(
    val publishedAt: Instant,
    val promotionId: PromotionId,
    val productId: ProductId,
    val country: Country,
) : PromotionEvent()

data class PromotionDeactivated(
    val publishedAt: Instant,
    val promotionId: PromotionId,
) : PromotionEvent()
