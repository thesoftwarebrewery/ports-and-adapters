package softwarebrewery.demo.domain.api

import softwarebrewery.demo.domain.*

@PrimaryPort
interface DomainApi {
    fun handle(offerCreated: OfferCreated)
    fun handle(offerDeleted: OfferDeleted)
    fun handle(promotionActivated: PromotionActivated)
    fun handle(promotionDeactivated: PromotionDeactivated)
}
