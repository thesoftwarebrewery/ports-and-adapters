package softwarebrewery.demo.domain.model

typealias OfferId = String

interface Offer {
    val offerId: OfferId
    val productId: ProductId
    val country: Country
}
