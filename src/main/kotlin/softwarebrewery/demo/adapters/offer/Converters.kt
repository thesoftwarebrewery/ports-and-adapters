package softwarebrewery.demo.adapters.offer

import softwarebrewery.demo.domain.api.*

fun ExternalOfferCreated.toDomainOfferCreated() =
    OfferCreated(
        publishedAt = this.createdAt!!,
        offerId = this.offerId!!,
        productId = this.productId!!,
        country = this.country!!,
    )
