package softwarebrewery.demo.domain

import softwarebrewery.demo.domain.model.Offer
import softwarebrewery.demo.domain.model.Promotion
import softwarebrewery.demo.domain.ports.*

class DomainHandler(
    private val offerRepository: OfferRepository,
    private val promotionRepository: PromotionRepository,
    private val offerPromotionListener: OfferPromotionListener,
    private val clock: Clock,
) : DomainApi {

    private val offerPromotionLinker = OfferPromotionLinker(
        offerRepository = offerRepository,
        promotionRepository = promotionRepository,
        offerPromotionListener = offerPromotionListener,
        clock = clock,
    )

    override fun handle(offerCreated: OfferCreated) {
        offerRepository.saveOrUpdate(
            Offer(
                publishedAt = offerCreated.publishedAt,
                offerId = offerCreated.offerId,
                productId = offerCreated.productId,
                country = offerCreated.country,
            )
        )?.let { offerPromotionLinker.linkPromosToOffer(it.value) }
    }

    override fun handle(offerDeleted: OfferDeleted) {
        throw UnsupportedOperationException()
    }

    override fun handle(promotionActivated: PromotionActivated) {
        promotionRepository.saveOrUpdate(
            Promotion(
                publishedAt = promotionActivated.publishedAt,
                promotionId = promotionActivated.promotionId,
                productId = promotionActivated.productId,
                country = promotionActivated.country,
            )
        )?.let { offerPromotionLinker.linkOffersToPromo(it.value) }
    }

    override fun handle(promotionDeactivated: PromotionDeactivated) {
        throw UnsupportedOperationException()
    }
}
