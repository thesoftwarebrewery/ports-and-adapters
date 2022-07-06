package softwarebrewery.demo.domain

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import softwarebrewery.demo.domain.model.OfferPromoted

class FunctionalTest {

    private val domain = DomainFixture()

    @Test
    fun `given existing offer when matching promo is activated then notifies offer promoted`() {
        val offerCreated = anOfferCreated()
        val promotionActivated = aPromotionActivated(
            productId = offerCreated.productId,
            country = offerCreated.country,
        )
        domain.handle(offerCreated)

        domain.handle(promotionActivated)

        domain.assertNoOffersDemoted()
        domain.assertOffersPromoted(OfferPromoted(
            publishedAt = domain.clock(), // promoted when 'promotion activated' was handled
            offerId = offerCreated.offerId,
            promotionId = promotionActivated.promotionId,
        ))
    }

    @Test
    fun `given existing offer when non-matching promo is activated then ignores`() {
        fail("not implemented")
    }

    @Test
    fun `given existing promo when matching offer is created then notifies offer promoted`() {
        fail("not implemented")
    }

    @Test
    fun `given existing promo when non-matching offer is created then ignores`() {
        fail("not implemented")
    }

    @Test
    fun `given offer promoted when promo is deleted then notifies offer demoted`() {
        fail("not implemented")
    }
}
