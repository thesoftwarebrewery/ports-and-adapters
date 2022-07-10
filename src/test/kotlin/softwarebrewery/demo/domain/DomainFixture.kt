package softwarebrewery.demo.domain

import org.assertj.core.api.Assertions.*
import softwarebrewery.demo.adapters.application.*
import softwarebrewery.demo.domain.model.*
import softwarebrewery.demo.domain.ports.*
import java.time.*
import kotlin.time.Duration.Companion.seconds

class DomainFixture {

    private val clock = FakeClock()
    private val offerRepository = InMemOfferRepository(clock)
    private val promoRepository = InMemPromoRepository(clock)
    private val offerPromotionListener = InMemOfferPromoListener()
    private val domainHandler = DomainHandler(
        offerRepository = offerRepository,
        promoRepository = promoRepository,
        offerPromotionLinker = DirectOfferPromoLinker(
            offerRepository = offerRepository,
            promoRepository = promoRepository,
            offerPromoListener = offerPromotionListener,
            clock = clock,
        ),
    )

    val time get() : Instant = clock()

    fun handle(vararg messages: Any) = messages.forEach {
        clock.forward(1.seconds)
        dispatch(it, domainHandler)
    }

    private fun dispatch(message: Any, receiver: Any) {
        val handlerMethod = receiver::class.java.methods.singleOrNull {
            it.name.equals("handle") && it.parameterCount == 1 && it.parameterTypes[0].isInstance(message)
        } ?: throw NoSuchMethodException("${receiver::class.simpleName}.handle(${message::class.simpleName})")

        handlerMethod.invoke(receiver, message)
    }

    fun assertOffersPromoted(vararg events: OfferPromoted) {
        assertThat(offerPromotionListener.offersPromoted).containsExactly(*events)
    }

    fun assertNoOffersPromoted() = assertThat(offerPromotionListener.offersPromoted).isEmpty()

    fun assertNoOffersDemoted() = assertThat(offerPromotionListener.offersDemoted).isEmpty()

}
