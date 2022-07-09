package softwarebrewery.demo.domain

import softwarebrewery.demo.domain.model.*
import java.time.*

@PrimaryPort
interface DomainApi {
    fun handle(event: OfferCreated)
    fun handle(event: OfferDeleted)
    fun handle(event: PromotionActivated)
    fun handle(event: PromotionDeactivated)
}

data class OfferCreated(
    val publishedAt: Instant,
    val offerId: OfferId,
    val productId: ProductId,
    val country: Country,
)

data class OfferDeleted(
    val publishedAt: Instant,
    val id: OfferId,
    val country: Country,
)

data class PromotionActivated(
    val publishedAt: Instant,
    val promotionId: PromotionId,
    val productId: ProductId,
    val country: Country,
)

data class PromotionDeactivated(
    val publishedAt: Instant,
    val promotionId: PromotionId,
)
