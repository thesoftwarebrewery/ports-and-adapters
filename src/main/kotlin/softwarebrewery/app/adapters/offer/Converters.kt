package softwarebrewery.app.adapters.offer

import softwarebrewery.app.domain.*

fun ExternalOfferCreated.toDomainOfferCreated() =
    OfferCreated(
        publishedAt = this.createdAt!!,
        offerId = this.offerId!!,
        productId = this.productId!!,
        country = this.country!!,
    )
