package softwarebrewery.app.adapters.application

import softwarebrewery.app.domain.model.*
import java.time.*

data class VersionedPromo(
    override val promotionId: PromotionId,
    override val productId: ProductId,
    override val country: Country,
    val modifiedAt: Instant?,
) : Promo
