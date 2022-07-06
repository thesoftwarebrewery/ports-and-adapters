package softwarebrewery.demo.domain.ports

import softwarebrewery.demo.domain.ProductId
import softwarebrewery.demo.domain.model.Promotion

interface PromotionRepository {
    fun saveOrUpdate(promotion: Promotion): Modified<Promotion>?
    fun findByProductId(productId: ProductId): Collection<Promotion>
}
