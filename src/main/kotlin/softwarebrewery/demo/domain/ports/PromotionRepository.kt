package softwarebrewery.demo.domain.ports

import softwarebrewery.demo.domain.*
import softwarebrewery.demo.domain.model.*

@SecondaryPort
interface PromotionRepository {
    fun insert(promotion: Promotion): Modified<Promotion>?
    fun findByProductId(productId: ProductId): Collection<Promotion>
}
