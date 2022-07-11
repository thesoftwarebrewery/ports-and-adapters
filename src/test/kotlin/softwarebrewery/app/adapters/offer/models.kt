package softwarebrewery.app.adapters.offer

import softwarebrewery.app.infra.*
import java.time.*

fun anExternalOfferCreated(
    createdAt: Instant = Instant.now(),
    offerId: String = namedRandom("offer"),
    productId: String = namedRandom("product"),
    country: String = Country.values().random().name,
) = ExternalOfferCreated(
    createdAt = createdAt,
    offerId = offerId,
    productId = productId,
    country = country,
)

enum class Country { BE, NL }
