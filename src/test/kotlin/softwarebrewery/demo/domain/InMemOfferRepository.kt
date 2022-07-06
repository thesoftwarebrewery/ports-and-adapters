package softwarebrewery.demo.domain

import softwarebrewery.demo.domain.ports.OfferRepository
import softwarebrewery.demo.domain.ports.OfferRepository.Offer
import java.util.concurrent.ConcurrentHashMap

class InMemOfferRepository : OfferRepository {

    private val records = ConcurrentHashMap<OfferId, Offer>()

    override fun saveOrUpdate(offer: Offer): Boolean {
        val current = records[offer.id]
        if (current != null && current.publishedAt.isAfter(offer.publishedAt)) {
            return false
        }

        records[offer.id] = offer
        return true
    }
}
