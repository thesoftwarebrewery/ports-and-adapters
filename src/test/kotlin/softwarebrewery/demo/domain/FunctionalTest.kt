package softwarebrewery.demo.domain

import org.junit.jupiter.api.Test
import softwarebrewery.demo.domain.ports.OfferPromoted
import java.time.Instant.now

class FunctionalTest {

    private val domain = DomainFixture()

    @Test
    fun `given existing offer when matching promo is activated then notifies offer promoted`() {
        val offerCreated = anOfferCreated()
        val promotionActivated = aPromotionActivated(country = offerCreated.country)
        domain.handle(offerCreated)

        domain.handle(promotionActivated)

        domain.assertNotifiedOf(
            OfferPromoted(
                publishedAt = now(),
                offerId = offerCreated.id,
                promotionId = promotionActivated.id,
            )
        )
    }

    @Test
    fun `given existing offer when non-matching promo is activated then ignores`() {
        throw NotImplementedError()
    }

    @Test
    fun `given existing promo when matching offer is created then notifies offer promoted`() {
        throw NotImplementedError()
    }

    @Test
    fun `given existing promo when non-matching offer is created then ignores`() {
        throw NotImplementedError()
    }

    @Test
    fun `given offer promoted when promo is deleted then notifies offer demoted`() {
        throw NotImplementedError()
    }
}
