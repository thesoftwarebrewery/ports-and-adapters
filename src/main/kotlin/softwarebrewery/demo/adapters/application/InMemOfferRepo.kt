package softwarebrewery.demo.adapters.application

import softwarebrewery.demo.domain.*
import softwarebrewery.demo.domain.model.*
import softwarebrewery.demo.domain.ports.*
import java.time.*
import java.util.concurrent.*

@DrivenAdapter
class InMemOfferRepo(
    private val clock: () -> Instant,
) : OfferRepo {

    private val records = ConcurrentHashMap<OfferId, Offer>()

    override fun new(offerId: OfferId, productId: ProductId, country: Country) =
        VersionedOffer(offerId, productId, country, clock())

    override fun insert(offer: Offer): Modified<Offer> {
        val txTime = clock()
        val inserted = VersionedOffer(
            offerId = offer.offerId,
            productId = offer.productId,
            country = offer.country,
            modifiedAt = txTime,
        )
        records[offer.offerId] = inserted
        return Modified(inserted, txTime)
    }

    override fun update(offer: Offer): Modified<Offer> = throw UnsupportedOperationException()

    override fun findByProductId(productId: ProductId) = records.filterValues { it.productId == productId }.values

}
