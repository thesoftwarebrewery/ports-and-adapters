package softwarebrewery.app.domain

import org.junit.jupiter.api.*
import softwarebrewery.app.domain.model.*

class DomainFunctionalTest {

    private val domain = DomainFixture()

    @Test
    fun `given existing offer when matching promo is activated then notifies offer promoted`() {
        val offerCreated = anOfferCreated()
        val matchingPromoActivated = aPromotionActivated(
            productId = offerCreated.productId,
            country = offerCreated.country,
        )
        domain.handle(offerCreated)

        domain.handle(matchingPromoActivated)

        domain.assertNoOffersDemoted()
        domain.assertOffersPromoted(
            OfferPromoted(
                publishedAt = domain.time,
                offerId = offerCreated.offerId,
                promotionId = matchingPromoActivated.promotionId,
                country = matchingPromoActivated.country,
            )
        )
    }

    @Test
    fun `given existing offer when non-matching promo is activated then ignores`() {
        val offerCreated = anOfferCreated()
        val nonMatchingPromoActivated = aPromotionActivated()
        domain.handle(offerCreated)

        domain.handle(nonMatchingPromoActivated)

        domain.assertNoOffersDemoted()
        domain.assertNoOffersPromoted()
    }

    @Test
    fun `given existing promo when matching offer is created then notifies offer promoted`() {
        val promoActivated = aPromotionActivated()
        val matchingOfferCreated = anOfferCreated(
            productId = promoActivated.productId,
            country = promoActivated.country,
        )
        domain.handle(promoActivated)

        domain.handle(matchingOfferCreated)

        domain.assertNoOffersDemoted()
        domain.assertOffersPromoted(
            OfferPromoted(
                publishedAt = domain.time,
                offerId = matchingOfferCreated.offerId,
                promotionId = promoActivated.promotionId,
                country = promoActivated.country,
            )
        )
    }

    @Test
    fun `given existing promo when non-matching offer is created then ignores`() {
        val promoActivated = aPromotionActivated()
        val nonMatchingOfferCreated = anOfferCreated()
        domain.handle(promoActivated)

        domain.handle(nonMatchingOfferCreated)

        domain.assertNoOffersDemoted()
        domain.assertNoOffersPromoted()
    }
}
