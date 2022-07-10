package softwarebrewery.demo.domain.ports

import softwarebrewery.demo.domain.model.*

interface OfferPromoLinker {
    fun linkOffersToPromo(promo: Promo)
    fun linkPromosToOffer(offer: Offer)
}
