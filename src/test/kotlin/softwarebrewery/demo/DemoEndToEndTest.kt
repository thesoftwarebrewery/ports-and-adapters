package softwarebrewery.demo

import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.*
import org.springframework.boot.test.context.*
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*
import org.springframework.context.annotation.*
import org.springframework.test.context.*
import softwarebrewery.demo.adapters.offer.*
import softwarebrewery.demo.adapters.promo.*
import softwarebrewery.demo.domain.model.*
import softwarebrewery.demo.infra.*
import java.time.Instant.*

@ActiveProfiles("it")
@Import(PubSubTestConfig::class)
@SpringBootTest(webEnvironment = NONE)
class DemoEndToEndTest(
    @Autowired val promoApplication: FakePromoApplication,
    @Autowired val offerApplication: FakeOfferApplication,
) {

    @Test
    fun `given existing offer when matching promo is activated then notifies offer promoted`() {
        val productId = namedRandom("product")
        val offerId = namedRandom("offer")
        val promotionId = namedRandom("promotionId")
        val country = "NL"
        offerApplication.createOffer(offerId = offerId, productId = productId, country = country)

        promoApplication.createPromotion(promotionId = promotionId, productId = productId, country = country)

        offerApplication.hasReceived(
            OfferPromoted(
                publishedAt = now(),
                offerId = offerId,
                promotionId = promotionId,
                country = country,
            )
        )
    }
}
