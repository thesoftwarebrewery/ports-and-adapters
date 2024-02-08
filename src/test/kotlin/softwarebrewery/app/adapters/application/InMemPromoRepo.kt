package softwarebrewery.app.adapters.application

import softwarebrewery.app.domain.*
import softwarebrewery.app.domain.model.*
import softwarebrewery.app.domain.ports.*
import java.time.*
import java.util.concurrent.*

@OutboundAdapter
class InMemPromoRepo(
    private val clock: () -> Instant,
) : PromoRepo {

    private val records = ConcurrentHashMap<PromotionId, Promo>()

    override fun new(promotionId: PromotionId, productId: ProductId, country: Country) =
        VersionedPromo(promotionId, productId, country, modifiedAt = null)

    override fun insert(promo: Promo): Modified<Promo> {
        val txTime = clock()
        val inserted = VersionedPromo(
            promotionId = promo.promotionId,
            productId = promo.productId,
            country = promo.country,
            modifiedAt = txTime,
        )
        records[promo.promotionId] = inserted
        return Modified(inserted, txTime)
    }

    override fun update(promo: Promo): Modified<Promo> = throw UnsupportedOperationException()

    override fun findByProductId(productId: ProductId) =
        records.filterValues { p -> p.productId == productId }.values

}
