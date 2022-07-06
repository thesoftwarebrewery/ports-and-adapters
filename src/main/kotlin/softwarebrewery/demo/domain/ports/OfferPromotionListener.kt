package softwarebrewery.demo.domain.ports

import softwarebrewery.demo.domain.*
import softwarebrewery.demo.domain.model.*

@SecondaryPort
interface OfferPromotionListener {
    fun handle(offerPromoted: OfferPromoted)
    fun handle(offerUnPromoted: OfferUnPromoted)
}
