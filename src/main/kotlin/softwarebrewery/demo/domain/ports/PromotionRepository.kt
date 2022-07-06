package softwarebrewery.demo.domain.ports

import softwarebrewery.demo.domain.*
import softwarebrewery.demo.domain.model.*

@SecondaryPort
interface PromotionRepository {
    fun saveOrUpdate(promotion: Promotion): Modified<Promotion>?
    fun findByProductId(productId: ProductId): Collection<Promotion>
}
