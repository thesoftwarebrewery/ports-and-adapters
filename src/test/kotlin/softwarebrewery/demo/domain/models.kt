package softwarebrewery.demo.domain

import java.time.Instant
import java.time.Instant.now

fun aCountry(): Country =
    setOf("BE", "NL").random()

fun anOfferCreated(
    publishedAt: Instant = now(),
    id: OfferId = randomString(),
    country: Country = aCountry(),
) = OfferCreated(
    publishedAt = publishedAt,
    id = id,
    country = country,
)

fun aPromotionActivated(
    publishedAt: Instant = now(),
    id: PromotionId = randomString(),
    country: Country = aCountry(),
) = PromotionActivated(
    publishedAt = publishedAt,
    id = id,
    country = country,
)
