package softwarebrewery.demo.domain.ports

import softwarebrewery.demo.domain.model.*

class InMemOfferPromoListener : OfferPromotionListener {

    private val promoted = mutableListOf<OfferPromoted>()
    private val demoted = mutableListOf<OfferUnPromoted>()

    val offersPromoted get() : List<OfferPromoted> = promoted
    val offersDemoted get() : List<OfferUnPromoted> = demoted

    override fun handle(offerPromoted: OfferPromoted) {
        promoted.add(offerPromoted)
    }

    override fun handle(offerUnPromoted: OfferUnPromoted) {
        demoted.add(offerUnPromoted)
    }
}