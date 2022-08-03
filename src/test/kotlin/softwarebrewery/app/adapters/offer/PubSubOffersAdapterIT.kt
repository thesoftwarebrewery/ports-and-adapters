package softwarebrewery.app.adapters.offer

import softwarebrewery.app.*
import softwarebrewery.app.domain.*
import softwarebrewery.infra.*
import softwarebrewery.infra.pubsub.*
import com.google.cloud.spring.pubsub.core.*
import org.assertj.core.api.Assertions.*
import org.awaitility.Awaitility.*
import org.junit.jupiter.api.*
import org.mockito.kotlin.*
import org.springframework.beans.factory.annotation.*
import org.springframework.boot.test.context.*
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*
import org.springframework.boot.test.mock.mockito.*
import org.springframework.context.annotation.*
import org.springframework.test.context.*
import java.time.Instant.*

@ActiveProfiles("it")
@Import(PubSubTestConfig::class)
@SpringBootTest(webEnvironment = NONE)
class PubSubOffersAdapterIT(
    @Value("\${pubsub.offer.offer-events.topic}") val offerEventsTopic: String,
    @Autowired val pubSub: PubSubOperations,
) {

    @MockBean
    private lateinit var domainApi: DomainApi

    @Test
    fun `when offer-created received then activates domain with promotion activated event`() {
        val offerId = namedRandom("offer")
        val productId = namedRandom("product")
        val promotionMessage = aPubSubMessage(
            attributes = mapOf("event_type" to "OFFER_CREATED"),
            data = """
                {
                  "createdAt": "2022-07-05T11:00:00.000000Z",
                  "offerId": "$offerId",
                  "productId": "$productId",
                  "country": "NL"
                }
            """.trimIndent()
        )

        pubSub.publishSync(offerEventsTopic, promotionMessage)

        await().untilAsserted {
            val domainMessage = argumentCaptor<OfferCreated>()
            verify(domainApi).handle(domainMessage.capture())
            verifyNoMoreInteractions(domainApi)
            assertThat(domainMessage.firstValue).isEqualTo(
                OfferCreated(
                    publishedAt = parse("2022-07-05T11:00:00.000000Z"),
                    offerId = offerId,
                    productId = productId,
                    country = "NL",
                ),
            )
        }
    }
}
