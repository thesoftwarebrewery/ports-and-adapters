package softwarebrewery.demo.adapters.promo

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
import softwarebrewery.demo.adapters.*
import softwarebrewery.demo.domain.api.*
import softwarebrewery.demo.technical.*
import java.time.Instant.*

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = NONE)
@Import(PubSubTestConfig::class)
class PubSubPromotionAdapterIT(
    @Value("\${pubsub.promo.promotion-events.topic}") val promotionEventsTopic: String,
    @Autowired val pubSub: PubSubOperations,
) {

    @MockBean
    private lateinit var domainApi: DomainApi

    @Test
    fun `when promotion-created received then activates domain with promotion activated event`() {
        val promotionMessageJson = """
            {
              "timestampUtc": "2022-07-05T10:00:00.000000Z",
              "changeType": "CREATE",
              "promotionId": 69,
              "productId": 42,
              "countries": ["BE"]
            }
        """.trimIndent()

        pubSub.publishSync(promotionEventsTopic, aPubSubMessage(data = promotionMessageJson))

        await().untilAsserted {
            val domainMessage = argumentCaptor<PromotionActivated>()

            verify(domainApi).handle(domainMessage.capture())
            verifyNoMoreInteractions(domainMessage)

            assertThat(domainMessage.firstValue).isEqualTo(
                PromotionActivated(
                    publishedAt = parse("2022-07-05T10:00:00.000000Z"),
                    promotionId = "69",
                    productId = "42",
                    country = "BE",
                ),
            )
        }
    }
}
