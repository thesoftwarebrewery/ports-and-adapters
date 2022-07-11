package softwarebrewery.app.domain.model

import softwarebrewery.app.domain.*
import softwarebrewery.app.infra.*
import java.time.*
import java.time.Instant.*

fun aCountry(): Country =
    setOf("BE", "NL").random()

fun anOfferCreated(
    publishedAt: Instant = now(),
    offerId: OfferId = namedRandom("offer"),
    productId: OfferId = namedRandom("product"),
    country: Country = aCountry(),
) = OfferCreated(
    publishedAt = publishedAt,
    offerId = offerId,
    productId = productId,
    country = country,
)

fun aPromotionActivated(
    publishedAt: Instant = now(),
    promotionId: PromotionId = namedRandom("promo"),
    productId: OfferId = namedRandom("product"),
    country: Country = aCountry(),
) = PromotionActivated(
    publishedAt = publishedAt,
    promotionId = promotionId,
    productId = productId,
    country = country,
)

fun aPromotionDeactivated(
    publishedAt: Instant = now(),
    promotionId: PromotionId = namedRandom("promo"),
) = PromotionDeactivated(
    publishedAt = publishedAt,
    promotionId = promotionId,
)
