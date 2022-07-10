package softwarebrewery.demo.domain.ports

import softwarebrewery.demo.domain.*
import softwarebrewery.demo.domain.model.*

@SecondaryPort
interface PromoRepository {

    fun new(promotionId: PromotionId, productId: ProductId, country: Country): Promo

    fun insert(promo: Promo): Modified<Promo>
    fun update(promo: Promo): Modified<Promo>
    fun findByProductId(productId: ProductId): Collection<Promo>

}
