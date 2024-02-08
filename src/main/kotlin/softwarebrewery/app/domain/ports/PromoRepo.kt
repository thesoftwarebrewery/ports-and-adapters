package softwarebrewery.app.domain.ports

import softwarebrewery.app.domain.*
import softwarebrewery.app.domain.model.*

@OutboundPort
interface PromoRepo {

    fun new(promotionId: PromotionId, productId: ProductId, country: Country): Promo

    fun insert(promo: Promo): Modified<Promo>
    fun update(promo: Promo): Modified<Promo>
    fun findByProductId(productId: ProductId): Collection<Promo>

}
