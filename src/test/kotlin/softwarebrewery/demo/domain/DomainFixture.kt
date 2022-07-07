package softwarebrewery.demo.domain

import org.assertj.core.api.Assertions.*
import softwarebrewery.demo.adapters.application.*
import softwarebrewery.demo.domain.model.*
import softwarebrewery.demo.domain.ports.*
import kotlin.time.Duration.Companion.seconds

class DomainFixture {

    val clock = FakeClock()

    private val offerRepository = InMemOfferRepository(clock)
    private val promotionRepository = InMemPromotionRepository(clock)
    private val offerPromotionListener = InMemOfferPromoListener()
    private val domainHandler = DomainHandler(
        offerRepository = offerRepository,
        promotionRepository = promotionRepository,
        offerPromotionListener = offerPromotionListener,
        clock = clock,
    )

    fun handle(vararg messages: Any) = messages.forEach {
        clock.advance(1.seconds)
        dispatch(it, domainHandler)
    }

    private fun dispatch(message: Any, receiver: Any) {
        val handlerMethod = receiver.javaClass.methods.singleOrNull {
            it.name.equals("handle") && it.parameterCount == 1 && it.parameterTypes[0].isInstance(message)
        } ?: throw UnsupportedOperationException("'${receiver.javaClass.simpleName}' has no 'handle(${message.javaClass.simpleName})'")

        handlerMethod.invoke(receiver, message)
    }

    fun assertOffersPromoted(vararg events: OfferPromoted) {
        assertThat(offerPromotionListener.offersPromoted).containsExactly(*events)
    }

    fun assertNoOffersPromoted() = assertThat(offerPromotionListener.offersPromoted).isEmpty()

    fun assertNoOffersDemoted() = assertThat(offerPromotionListener.offersDemoted).isEmpty()

}
