package softwarebrewery.demo.domain.model

import softwarebrewery.demo.domain.Country
import softwarebrewery.demo.domain.ProductId
import softwarebrewery.demo.domain.PromotionId
import java.time.Instant

data class Promotion(
    val publishedAt: Instant,
    val promotionId: PromotionId,
    val productId: ProductId,
    val country: Country,
)

fun Promotion.appliesTo(offer: Offer): Boolean {
    return this.productId == offer.productId
            && this.country == offer.country
}
