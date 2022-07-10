package softwarebrewery.app.domain.ports

import softwarebrewery.app.domain.*
import softwarebrewery.app.domain.model.*

@SecondaryPort
interface OfferPromoListener {
    fun handle(event: OfferPromoted)
    fun handle(event: OfferDemoted)
}
