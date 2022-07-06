package softwarebrewery.demo.domain

import java.time.Instant

typealias Country = String
typealias OfferId = String
typealias ProductId = String
typealias PromotionId = String

interface DomainApi {
    fun handle(offerCreated: OfferCreated)
    fun handle(offerDeleted: OfferDeleted)
    fun handle(promotionActivated: PromotionActivated)
    fun handle(promotionDeactivated: PromotionDeactivated)
}

data class OfferCreated(
    val publishedAt: Instant,
    val offerId: OfferId,
    val productId: ProductId,
    val country: Country
)

data class OfferDeleted(
    val publishedAt: Instant,
    val id: OfferId,
    val country: Country
)

data class PromotionActivated(
    val publishedAt: Instant,
    val promotionId: PromotionId,
    val productId: ProductId,
    val country: Country
)

data class PromotionDeactivated(
    val publishedAt: Instant,
    val promotionId: PromotionId,
)

/*
data class Message<T>(
    val payload: T,
    val receivedAt: Instant,
)
*/
