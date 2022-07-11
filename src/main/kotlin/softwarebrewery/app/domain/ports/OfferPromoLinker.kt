package softwarebrewery.app.domain.ports

import softwarebrewery.app.domain.*
import softwarebrewery.app.domain.model.*

@SecondaryPort
interface OfferPromoLinker {
    fun linkPromosToOffer(offer: Offer)
    fun linkOffersToPromo(promo: Promo)
}
