import softwarebrewery.demo.*
import softwarebrewery.demo.adapters.promo.*
import java.time.*
import java.time.Instant.*

fun aPromotionMessage(
    timestamp: Instant = now(),
    promotionId: String = namedRandom("promotion"),
    productId: String = namedRandom("product"),
    country: String = Country.values().random().name,
) = PromotionMessage(
    timestampUtc = timestamp,
    promotionId = promotionId,
    productId = productId,
    country = country,
)
