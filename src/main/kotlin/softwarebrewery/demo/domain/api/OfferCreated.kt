package softwarebrewery.demo.domain.api

import softwarebrewery.demo.domain.model.*
import java.time.*

data class OfferCreated(
    val publishedAt: Instant,
    val offerId: OfferId,
    val productId: ProductId,
    val country: Country
)
