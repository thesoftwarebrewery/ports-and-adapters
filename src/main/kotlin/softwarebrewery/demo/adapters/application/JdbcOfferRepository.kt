package softwarebrewery.demo.adapters.application

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import softwarebrewery.demo.domain.model.*
import softwarebrewery.demo.domain.ports.*

class JdbcOfferRepository(
    db: NamedParameterJdbcTemplate,
) : OfferRepository {

    override fun insert(offer: Offer): Modified<Offer>? {
        TODO("Not yet implemented")
    }

    override fun findByProductId(productId: ProductId): Collection<Offer> {
        TODO("Not yet implemented")
    }
}
