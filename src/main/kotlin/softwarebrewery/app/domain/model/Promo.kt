package softwarebrewery.app.domain.model

import java.time.*

typealias PromotionId = String

interface Promo {
    val promotionId: PromotionId
    val productId: ProductId
    val country: Country
}

data class VersionedPromo(
    override val promotionId: PromotionId,
    override val productId: ProductId,
    override val country: Country,
    val modifiedAt: Instant?,
) : Promo

fun Promo.appliesTo(offer: Offer) =
    productId == offer.productId
            && country == offer.country
