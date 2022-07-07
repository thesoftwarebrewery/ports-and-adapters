import softwarebrewery.demo.adapters.promo.*
import softwarebrewery.demo.testing.*
import java.time.*

fun aPromotionMessage(
    timestamp: Instant = Instant.now(),
    promotionId: Int = randomInt(),
    productId: Int = randomInt(),
    country: String = Country.values().random().name,
) = PromotionMessage(
    timestampUtc = timestamp,
    promotionId = promotionId,
    productId = productId,
    country = country,
)
