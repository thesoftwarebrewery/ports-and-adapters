package softwarebrewery.demo.domain.ports

import softwarebrewery.demo.domain.ProductId
import softwarebrewery.demo.domain.model.Offer

interface OfferRepository {
    fun saveOrUpdate(offer: Offer): Modified<Offer>?
    fun findByProductId(productId: ProductId) : Collection<Offer>
}
