package softwarebrewery.demo.adapters.application

import softwarebrewery.demo.domain.*
import softwarebrewery.demo.domain.model.*
import softwarebrewery.demo.domain.ports.*

@DrivenAdapter
class InMemOfferPromoListener : OfferPromoListener {

    private val promoted = mutableListOf<OfferPromoted>()
    private val demoted = mutableListOf<OfferUnPromoted>()

    val offersPromoted get() : List<OfferPromoted> = promoted
    val offersDemoted get() : List<OfferUnPromoted> = demoted

    override fun handle(event: OfferPromoted) {
        promoted.add(event)
    }

    override fun handle(event: OfferUnPromoted) {
        demoted.add(event)
    }
}
