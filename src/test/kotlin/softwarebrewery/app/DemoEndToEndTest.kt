package softwarebrewery.app

import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.*
import org.springframework.boot.test.context.*
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*
import org.springframework.context.annotation.*
import org.springframework.test.context.*
import softwarebrewery.app.adapters.offer.*
import softwarebrewery.app.adapters.promo.*
import softwarebrewery.app.infra.pubsub.*
import java.time.Instant.*

//@ResetDatabase
@ActiveProfiles("it")
@Import(PubSubTestConfig::class)
@SpringBootTest(webEnvironment = NONE)
class ApplicationEndToEndTest(
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
