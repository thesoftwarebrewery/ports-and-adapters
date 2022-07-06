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
    @Disabled("not implemented")
    fun `given existing promo when matching offer is created then notifies offer promoted`() {
    }

    @Test
    @Disabled("not implemented")
    fun `given existing promo when non-matching offer is created then ignores`() {
    }

    @Test
    @Disabled("not implemented")
    fun `given offer promoted when promo is deleted then notifies offer demoted`() {
    }
}
