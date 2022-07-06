package softwarebrewery.demo.domain.ports

import softwarebrewery.demo.domain.model.*
import java.util.concurrent.*

class InMemPromotionRepository(
    private val clock: Clock,
) : PromotionRepository {

    private val records = ConcurrentHashMap<PromotionId, Promotion>()

    override fun saveOrUpdate(promotion: Promotion): Modified<Promotion>? {
        val current = records[promotion.promotionId]

        if (current != null && current.publishedAt.isAfter(promotion.publishedAt)) {
            return null
        }

        val txTime = clock()
        records[promotion.promotionId] = promotion

        return Modified(promotion, txTime)
    }

    override fun findByProductId(productId: ProductId) =
        records.filterValues { p -> p.productId == productId }.values

}
