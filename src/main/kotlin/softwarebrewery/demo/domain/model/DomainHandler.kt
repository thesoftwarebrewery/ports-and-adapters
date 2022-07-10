package softwarebrewery.demo.domain.model

import org.springframework.transaction.annotation.*
import softwarebrewery.demo.domain.*
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

    override fun handle(event: OfferCreated) {
        offerRepository.insert(
            Offer(
                publishedAt = event.publishedAt,
                offerId = event.offerId,
                productId = event.productId,
                country = event.country,
            )
        )?.let { offerPromotionLinker.linkPromosToOffer(it.it) }
    }

    override fun handle(event: OfferDeleted): Unit = throw UnsupportedOperationException()

    override fun handle(event: PromotionActivated) {
        promotionRepository.insert(
            Promotion(
                publishedAt = event.publishedAt,
                promotionId = event.promotionId,
                productId = event.productId,
                country = event.country,
            )
        )?.let { offerPromotionLinker.linkOffersToPromo(it.it) }
    }

    override fun handle(event: PromotionDeactivated): Unit = throw UnsupportedOperationException()

}
