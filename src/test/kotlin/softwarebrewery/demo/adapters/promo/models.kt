import softwarebrewery.demo.adapters.promo.*
import softwarebrewery.demo.testing.*
import java.time.*

fun aPromotionMessage(
    timestamp: Instant = Instant.now(),
    changeType: String = ChangeType.values().random().name,
    productId: Int = randomInt(),
    countries: Set<String> = setOf(Country.values().random().name),
) = PromotionMessage(
    timestampUtc = timestamp,
    changeType = changeType,
    productId = productId,
    countries = countries,
)
