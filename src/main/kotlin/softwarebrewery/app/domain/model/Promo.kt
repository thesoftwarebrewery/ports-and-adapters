package softwarebrewery.app.domain.model

typealias PromotionId = String

interface Promo {
    val promotionId: PromotionId
    val productId: ProductId
    val country: Country
}

fun Promo.appliesTo(offer: Offer) =
    productId == offer.productId
            && country == offer.country
