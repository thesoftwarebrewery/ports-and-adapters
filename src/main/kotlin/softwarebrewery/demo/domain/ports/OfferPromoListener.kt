package softwarebrewery.demo.domain.ports

import softwarebrewery.demo.domain.*
import softwarebrewery.demo.domain.model.*

@SecondaryPort
interface OfferPromoListener {
    fun handle(event: OfferPromoted)
    fun handle(event: OfferDemoted)
}
