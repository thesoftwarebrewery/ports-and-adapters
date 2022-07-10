package softwarebrewery.app.adapters.promo

import softwarebrewery.app.*
import java.time.*
import java.time.Instant.*

fun anExternalPromotionMessage(
    timestamp: Instant = now(),
    promotionId: String = namedRandom("promotion"),
    productId: String = namedRandom("product"),
    country: String = setOf("BE", "NL").random(),
) = ExternalPromotionMessage(
    timestampUtc = timestamp,
    promotionId = promotionId,
    productId = productId,
    country = country,
)
