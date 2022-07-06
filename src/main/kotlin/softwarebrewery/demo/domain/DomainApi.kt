package softwarebrewery.demo.domain

import softwarebrewery.demo.domain.ports.OfferRepository
import softwarebrewery.demo.domain.ports.OfferRepository.Offer
import java.time.Instant

typealias Country = String
typealias OfferId = String
typealias PromotionId = String

interface DomainApi {
    fun handle(offerCreated: OfferCreated)
    fun handle(offerDeleted: OfferDeleted)
    fun handle(promotionActivated: PromotionActivated)
    fun handle(promotionDeactivated: PromotionDeactivated)
}

class SimpleDomainHandler(
    private val offerRepository: OfferRepository,
    private val promotionApplicabi: OfferRepository,
) : DomainApi {

    override fun handle(offerCreated: OfferCreated) {
        offerRepository.saveOrUpdate(
            Offer(
                publishedAt = offerCreated.publishedAt,
                id = offerCreated.id,
                country = offerCreated.country,
            )
        )
    }

    override fun handle(offerDeleted: OfferDeleted) {
        TODO("Not yet implemented")
    }

    override fun handle(promotionActivated: PromotionActivated) {
        TODO("Not yet implemented")
    }

    override fun handle(promotionDeactivated: PromotionDeactivated) {
        TODO("Not yet implemented")
    }
}

data class OfferCreated(
    val publishedAt: Instant,
    val id: OfferId,
    val country: Country
)

data class OfferDeleted(
    val publishedAt: Instant,
    val id: OfferId,
    val country: Country
)

data class PromotionActivated(
    val publishedAt: Instant,
    val id: PromotionId,
    val country: Country
)

data class PromotionDeactivated(
    val publishedAt: Instant,
    val id: PromotionId,
)
