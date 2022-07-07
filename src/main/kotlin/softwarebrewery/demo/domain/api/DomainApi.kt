package softwarebrewery.demo.domain.api

import softwarebrewery.demo.domain.*

@PrimaryPort
interface DomainApi : OfferApi, PromotionApi

interface PromotionApi {
    fun handle(promotionActivated: PromotionActivated)
    fun handle(promotionDeactivated: PromotionDeactivated)
}

interface OfferApi {
    fun handle(offerCreated: OfferCreated)
    fun handle(offerDeleted: OfferDeleted)
}
