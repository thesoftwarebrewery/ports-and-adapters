package softwarebrewery.demo.domain.ports

import softwarebrewery.demo.domain.model.*

interface OfferPromoLinker {
    fun linkPromosToOffer(offer: Offer)
    fun linkOffersToPromo(promo: Promo)
}
