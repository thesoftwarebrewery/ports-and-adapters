package softwarebrewery.demo.adapters.application

import softwarebrewery.demo.domain.model.*
import java.time.*

data class VersionedOffer(
    override val offerId: OfferId,
    override val productId: ProductId,
    override val country: Country,
    val modifiedAt: Instant?,
) : Offer {
    override fun clone(
        offerId: OfferId,
        productId: ProductId,
        country: Country
    ): Offer = copy(offerId = offerId, productId = productId, country = country)
}
