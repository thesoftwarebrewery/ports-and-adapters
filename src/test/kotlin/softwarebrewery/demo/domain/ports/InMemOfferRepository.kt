package softwarebrewery.demo.domain.ports

import softwarebrewery.demo.domain.OfferId
import softwarebrewery.demo.domain.ProductId
import softwarebrewery.demo.domain.model.Offer
import java.util.concurrent.ConcurrentHashMap

class InMemOfferRepository(
    private val clock: Clock,
) : OfferRepository {

    private val records = ConcurrentHashMap<OfferId, Offer>()

    override fun saveOrUpdate(offer: Offer): Modified<Offer>? {
        val current = records[offer.offerId]

        if (current != null && current.publishedAt.isAfter(offer.publishedAt)) {
            return null
        }

        val txTime = clock()
        records[offer.offerId] = offer

        return Modified(offer, txTime)
    }

    override fun findByProductId(productId: ProductId) =
        records.filterValues { it.productId == productId }.values

}
