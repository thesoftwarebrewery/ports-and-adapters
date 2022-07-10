package softwarebrewery.demo.adapters.promo

import softwarebrewery.demo.*
import java.time.*
import java.time.Instant.*

fun aPromotionMessage(
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
