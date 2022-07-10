package softwarebrewery.demo.domain.model

typealias OfferId = String

interface Offer {

    val offerId: OfferId
    val productId: ProductId
    val country: Country

    fun clone(
        offerId: OfferId = this.offerId,
        productId: ProductId = this.productId,
        country: Country = this.country,
    ): Offer
}
