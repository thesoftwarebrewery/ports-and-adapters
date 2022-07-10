package softwarebrewery.demo.adapters.promo

import softwarebrewery.demo.domain.*

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
