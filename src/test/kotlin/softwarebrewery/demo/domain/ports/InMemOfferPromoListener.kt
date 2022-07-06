package softwarebrewery.demo.domain.ports

import softwarebrewery.demo.domain.model.OfferNoLongerPromoted
import softwarebrewery.demo.domain.model.OfferPromoted

class InMemOfferPromoListener : OfferPromotionListener {

    private val promoted = mutableListOf<OfferPromoted>()
    private val demoted = mutableListOf<OfferNoLongerPromoted>()

    val offersPromoted get() : List<OfferPromoted> = promoted
    val offersDemoted get() : List<OfferNoLongerPromoted> = demoted

    override fun handle(offerPromoted: OfferPromoted) {
        promoted.add(offerPromoted)
    }

    override fun handle(offerNoLongerPromoted: OfferNoLongerPromoted) {
        demoted.add(offerNoLongerPromoted)
    }
}
