package softwarebrewery.demo.domain.ports

import softwarebrewery.demo.domain.*
import softwarebrewery.demo.domain.model.*

@SecondaryPort
interface OfferRepository {
    fun insert(offer: Offer): Modified<Offer>?
    fun findByProductId(productId: ProductId): Collection<Offer>
}
