package softwarebrewery.demo

import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.*
import org.springframework.boot.test.context.*
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*
import org.springframework.context.annotation.*
import org.springframework.test.context.*
import softwarebrewery.demo.adapters.offer.*
import softwarebrewery.demo.adapters.promo.*
import softwarebrewery.demo.infra.jdbc.*
import softwarebrewery.demo.infra.pubsub.*
import java.time.Instant.*

@ResetDbForEachTest
@ActiveProfiles("it")
@Import(PubSubTestConfig::class)
@SpringBootTest(webEnvironment = NONE)
class DemoEndToEndTest(
    @Autowired val promoDomain: FakePromoDomain,
    @Autowired val offerDomain: FakeOfferDomain,
) {

    @Test
    fun `given existing offer when matching promo is activated then notifies offer promoted`() {
        val offer = anExternalOfferCreated()
        val promotion = anExternalPromotionMessage(productId = offer.productId!!, country = offer.country!!)
        val offerPromoted = ExternalOfferPromoted(
            publishedAt = now(),
            offerId = offer.offerId!!,
            promotionId = promotion.promotionId!!,
            country = promotion.country!!,
        )
        offerDomain.publishCreated(offer)

        promoDomain.publishCreated(promotion)

        offerDomain.hasReceived(offerPromoted)
    }
}
