package softwarebrewery.demo.domain.api

import softwarebrewery.demo.domain.model.*
import java.time.*

data class OfferDeleted(
    val publishedAt: Instant,
    val id: OfferId,
    val country: Country
)
