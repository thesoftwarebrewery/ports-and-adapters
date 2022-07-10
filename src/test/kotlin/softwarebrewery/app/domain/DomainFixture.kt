package softwarebrewery.app.domain

import org.assertj.core.api.Assertions.*
import softwarebrewery.app.adapters.application.*
import softwarebrewery.app.domain.model.*
import softwarebrewery.app.domain.ports.*
import java.time.*
import kotlin.time.Duration.Companion.seconds

class DomainFixture {

    private val clock = FakeClock()
    private val offerRepository = InMemOfferRepo(clock)
    private val promoRepository = InMemPromoRepo(clock)
    private val offerPromotionListener = InMemOfferPromoListener()
    private val domainHandler = DomainHandler(
        offerRepo = offerRepository,
        promoRepo = promoRepository,
        offerPromoLinker = DirectOfferPromoLinker(
            offerRepo = offerRepository,
            promoRepo = promoRepository,
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
