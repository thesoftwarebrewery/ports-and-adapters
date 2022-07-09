package softwarebrewery.demo.adapters.application

import softwarebrewery.demo.domain.model.*
import softwarebrewery.demo.domain.ports.*

class JdbcOfferRepository(

) : OfferRepository {

    override fun saveOrUpdate(offer: Offer): Modified<Offer>? {
        TODO("Not yet implemented")
    }

    override fun findByProductId(productId: ProductId): Collection<Offer> {
        TODO("Not yet implemented")
    }
}
