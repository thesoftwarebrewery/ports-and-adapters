package softwarebrewery.app.adapters.application

import softwarebrewery.app.domain.*
import softwarebrewery.app.domain.model.*
import softwarebrewery.app.domain.ports.*

@OutboundAdapter
class InMemOfferPromoAnnouncer : OfferPromoAnnouncer {

    private val promoted = mutableListOf<OfferPromoted>()
    private val demoted = mutableListOf<OfferDemoted>()

    val offersPromoted get() : List<OfferPromoted> = promoted
    val offersDemoted get() : List<OfferDemoted> = demoted

    override fun announce(event: OfferPromoted) {
        promoted.add(event)
    }

    override fun announce(event: OfferDemoted) {
        demoted.add(event)
    }
}
