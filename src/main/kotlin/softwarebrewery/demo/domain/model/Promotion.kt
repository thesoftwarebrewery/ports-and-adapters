package softwarebrewery.demo.domain.model

import java.time.*

typealias PromotionId = String

data class Promotion(
    val publishedAt: Instant,
    val promotionId: PromotionId,
    val productId: ProductId,
    val country: Country,
)

fun Promotion.appliesTo(offer: Offer) =
    productId == offer.productId
            && country == offer.country
