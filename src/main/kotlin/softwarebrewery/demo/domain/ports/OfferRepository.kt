package softwarebrewery.demo.domain.ports

import softwarebrewery.demo.domain.Country
import softwarebrewery.demo.domain.OfferId
import java.time.Instant

interface OfferRepository {
    fun saveOrUpdate(offer: Offer): Boolean
}

data class Offer(
    val publishedAt: Instant,
    val id: OfferId,
    val country: Country,
)
