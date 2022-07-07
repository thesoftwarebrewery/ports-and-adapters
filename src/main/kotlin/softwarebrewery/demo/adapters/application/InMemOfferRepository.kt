package softwarebrewery.demo.adapters.application

import softwarebrewery.demo.domain.*
import softwarebrewery.demo.domain.model.*
import softwarebrewery.demo.domain.ports.*
import java.time.*
import java.util.concurrent.*

@DrivenAdapter
class InMemOfferRepository(
    private val clock: () -> Instant,
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
