package softwarebrewery.demo.domain.model

import org.springframework.transaction.annotation.*
import softwarebrewery.demo.domain.api.*
import softwarebrewery.demo.domain.ports.*

@Transactional
class DomainHandler(
    private val offerRepository: OfferRepository,
    private val promotionRepository: PromotionRepository,
    offerPromoListener: OfferPromoListener,
    clock: Clock,
) : DomainApi {

    private val offerPromotionLinker = OfferPromotionLinker(
        offerRepository = offerRepository,
        promotionRepository = promotionRepository,
        offerPromoListener = offerPromoListener,
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
        )?.let { offerPromotionLinker.linkPromosToOffer(it.it) }
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
        )?.let { offerPromotionLinker.linkOffersToPromo(it.it) }
    }

    override fun handle(promotionDeactivated: PromotionDeactivated) {
        throw UnsupportedOperationException()
    }
}
