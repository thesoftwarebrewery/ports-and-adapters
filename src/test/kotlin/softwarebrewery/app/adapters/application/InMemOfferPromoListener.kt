package softwarebrewery.app.adapters.application

import softwarebrewery.app.domain.*
import softwarebrewery.app.domain.model.*
import softwarebrewery.app.domain.ports.*

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
