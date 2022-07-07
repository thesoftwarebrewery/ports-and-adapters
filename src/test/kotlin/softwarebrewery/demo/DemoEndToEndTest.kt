package softwarebrewery.demo

import aPromotionMessage
import anExternalOfferCreated
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.*
import org.springframework.context.annotation.*
import softwarebrewery.demo.adapters.application.*
import softwarebrewery.demo.adapters.offers.*
import softwarebrewery.demo.adapters.promo.*
import softwarebrewery.demo.infra.*

@Import(PubSubTestConfig::class)
class DemoEndToEndTest(
    @Autowired val promos: FakePromos,
    @Autowired val offers: FakeOffers,
    @Autowired val promotedOffersDashboard: PromotedOffersDashboard,
) {

    @Test
    fun `given existing offer when matching promo is activated then notifies offer promoted`() {
        val offerCreated = anExternalOfferCreated()
        val productId = offerCreated.productId!!
        val country = offerCreated.country!!
        val matchingPromotion = aPromotionMessage(
            productId = productId,
            country = country,
        )
        offers.publish(offerCreated)

        promos.publish(matchingPromotion)

        promotedOffersDashboard.showsPromoted(country, productId)
    }
}
