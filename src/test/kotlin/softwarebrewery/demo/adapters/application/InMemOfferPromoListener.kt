package softwarebrewery.demo.adapters.application

import softwarebrewery.demo.domain.*
import softwarebrewery.demo.domain.model.*
import softwarebrewery.demo.domain.ports.*

@DrivenAdapter
class InMemOfferPromoListener : OfferPromoListener {

    private val promoted = mutableListOf<OfferPromoted>()
    private val demoted = mutableListOf<OfferDemoted>()

    val offersPromoted get() : List<OfferPromoted> = promoted
    val offersDemoted get() : List<OfferDemoted> = demoted

    override fun handle(event: OfferPromoted) {
        promoted.add(event)
    }

    override fun handle(event: OfferDemoted) {
        demoted.add(event)
    }
}
