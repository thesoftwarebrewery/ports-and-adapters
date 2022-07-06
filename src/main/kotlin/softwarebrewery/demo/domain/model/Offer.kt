package softwarebrewery.demo.domain.model

import java.time.*

typealias OfferId = String

data class Offer(
    val publishedAt: Instant,
    val offerId: OfferId,
    val productId: ProductId,
    val country: Country,
)
