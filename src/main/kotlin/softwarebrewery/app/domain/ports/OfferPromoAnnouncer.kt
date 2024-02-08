package softwarebrewery.app.domain.ports

import softwarebrewery.app.domain.*
import softwarebrewery.app.domain.model.*

@OutboundPort
interface OfferPromoAnnouncer {
    fun announce(event: OfferPromoted)
    fun announce(event: OfferDemoted)
}
