package softwarebrewery.demo.domain.ports

import softwarebrewery.demo.domain.model.OfferNoLongerPromoted
import softwarebrewery.demo.domain.model.OfferPromoted

interface OfferPromotionListener {
    fun handle(offerPromoted: OfferPromoted)
    fun handle(offerNoLongerPromoted: OfferNoLongerPromoted)
}
