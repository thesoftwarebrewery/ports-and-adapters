package softwarebrewery.demo.domain

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import softwarebrewery.demo.domain.model.OfferPromoted

class FunctionalTest {

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
        domain.assertOffersPromoted(OfferPromoted(
            publishedAt = domain.clock(), // promoted when 'promotion activated' was handled
            offerId = offerCreated.offerId,
            promotionId = matchingPromoActivated.promotionId,
        ))
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
        domain.assertOffersPromoted(OfferPromoted(
            publishedAt = domain.clock(), // promoted when 'promotion activated' was handled
            offerId = matchingOfferCreated.offerId,
            promotionId = promoActivated.promotionId,
        ))
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

    @Test
    @Disabled("not implemented")
    fun `given offer promoted when promo is deleted then notifies offer demoted`() {
    }
}
