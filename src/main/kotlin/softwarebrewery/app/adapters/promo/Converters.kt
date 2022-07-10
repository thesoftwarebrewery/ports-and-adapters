package softwarebrewery.app.adapters.promo

import softwarebrewery.app.domain.*

fun ExternalPromotionMessage.toDomainPromotionActivated() =
    PromotionActivated(
        publishedAt = this.timestampUtc!!,
        promotionId = this.promotionId!!,
        productId = this.productId!!,
        country = this.country!!,
    )

fun ExternalPromotionMessage.toDomainPromotionDeactivated() =
    PromotionDeactivated(
        publishedAt = this.timestampUtc!!,
        promotionId = this.promotionId!!,
    )
