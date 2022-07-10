package softwarebrewery.app.domain.ports

import softwarebrewery.app.domain.model.*

interface OfferPromoLinker {
    fun linkPromosToOffer(offer: Offer)
    fun linkOffersToPromo(promo: Promo)
}
