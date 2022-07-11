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
    fun `when matching offer and promo are created then announces offer-promoted`() {
        val offerCreated = anExternalOfferCreated()
        val promotionCreated = anExternalPromotionMessage(
            productId = offerCreated.productId!!,
            country = offerCreated.country!!,
        )
        val offerPromoted = ExternalOfferPromoted(
            publishedAt = now(),
            offerId = offerCreated.offerId!!,
            promotionId = promotionCreated.promotionId!!,
            country = promotionCreated.country!!,
        )

        offerDomain.publish(offerCreated)
        promoDomain.publish(promotionCreated)

        offerDomain.hasReceived(offerPromoted)
    }
}
