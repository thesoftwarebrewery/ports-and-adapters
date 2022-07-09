package softwarebrewery.demo.domain.api

import softwarebrewery.demo.domain.*

@PrimaryPort
interface DomainApi {
    fun handle(event: OfferCreated)
    fun handle(event: OfferDeleted)
    fun handle(event: PromotionActivated)
    fun handle(event: PromotionDeactivated)
}
