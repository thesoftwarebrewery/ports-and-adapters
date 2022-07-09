package softwarebrewery.demo.adapters.promo

import softwarebrewery.demo.domain.*

fun PromotionMessage.toDomainPromotionActivated() =
    PromotionActivated(
        publishedAt = this.timestampUtc!!,
        promotionId = "${this.promotionId!!}",
        productId = "${this.productId!!}",
        country = this.country!!,
    )

fun PromotionMessage.toDomainPromotionDeactivated() =
    PromotionDeactivated(
        publishedAt = this.timestampUtc!!,
        promotionId = "${this.promotionId!!}",
    )
