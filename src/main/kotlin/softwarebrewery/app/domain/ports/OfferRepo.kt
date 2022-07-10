package softwarebrewery.app.domain.ports

import softwarebrewery.app.domain.*
import softwarebrewery.app.domain.model.*

@SecondaryPort
interface OfferRepo {

    fun new(offerId: OfferId, productId: ProductId, country: Country): Offer

    fun insert(offer: Offer): Modified<Offer>
    fun update(offer: Offer): Modified<Offer>
    fun findByProductId(productId: ProductId): Collection<Offer>

}
