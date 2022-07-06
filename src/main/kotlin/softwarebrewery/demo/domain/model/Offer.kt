package softwarebrewery.demo.domain.model

import softwarebrewery.demo.domain.Country
import softwarebrewery.demo.domain.OfferId
import softwarebrewery.demo.domain.ProductId
import java.time.Instant

data class Offer(
    val publishedAt: Instant,
    val offerId: OfferId,
    val productId: ProductId,
    val country: Country,
)
