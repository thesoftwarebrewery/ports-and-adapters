package softwarebrewery.demo.domain.api

import softwarebrewery.demo.domain.model.*
import java.time.*

sealed class OfferEvent

data class OfferCreated(
    val publishedAt: Instant,
    val offerId: OfferId,
    val productId: ProductId,
    val country: Country,
)

data class OfferDeleted(
    val publishedAt: Instant,
    val id: OfferId,
    val country: Country,
)
