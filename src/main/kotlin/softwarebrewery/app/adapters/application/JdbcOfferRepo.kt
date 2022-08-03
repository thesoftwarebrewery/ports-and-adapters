package softwarebrewery.app.adapters.application

import softwarebrewery.app.domain.model.*
import softwarebrewery.app.domain.ports.*
import org.springframework.dao.*
import org.springframework.jdbc.core.*
import org.springframework.jdbc.core.namedparam.*
import java.sql.*
import java.time.*

class JdbcOfferRepo(
    private val db: NamedParameterJdbcTemplate,
    private val clock: () -> Instant,
) : OfferRepo {

    override fun new(offerId: OfferId, productId: ProductId, country: Country) =
        VersionedOffer(offerId, productId, country, modifiedAt = null)

    override fun insert(offer: Offer): Modified<Offer> {
        val versioned = offer as VersionedOffer

        val query = """
            insert into offers (offer_id, product_id, country, created_at, modified_at)
            values (:offer_id, :product_id, :country, :created_at, :modified_at)
        """.trimIndent()
        val txTime = clock()
        val params = mapOf(
            "offer_id" to versioned.offerId,
            "product_id" to versioned.productId,
            "country" to versioned.country,
            "created_at" to Timestamp.from(txTime),
            "modified_at" to Timestamp.from(txTime),
        )

        db.update(query, params)

        return Modified(it = versioned.copy(modifiedAt = txTime), at = txTime)
    }

    override fun update(offer: Offer): Modified<Offer> {
        val versioned = offer as VersionedOffer

        val query = """
            update offers set
                country = :country,
                product_id = :product_id,
                modified_at = :new_modified_at
            where
                offer_id = :offer_id
                and modified_at = :old_modified_at
                and :new_modified_at > :old_modified_at
        """.trimIndent()
        val txTime = clock()
        val params = mapOf(
            "offer_id" to versioned.offerId,
            "country" to versioned.country,
            "product_id" to versioned.productId,
            "old_modified_at" to Timestamp.from(versioned.modifiedAt!!),
            "new_modified_at" to Timestamp.from(txTime),
        )

        val affected = db.update(query, params)
        if (affected != 1) {
            throw OptimisticLockingFailureException("offers")
        }
        return Modified(it = versioned.copy(modifiedAt = txTime), at = txTime)
    }

    override fun findByProductId(productId: ProductId): Collection<Offer> {
        val query = """
            select offer_id, product_id, country, created_at, modified_at
            from offers
            where product_id = :product_id 
        """.trimIndent()
        val params = mapOf("product_id" to productId)
        return db.query(query, params, offerMapper)
    }

    companion object {

        private val offerMapper = RowMapper { rs, _ ->
            VersionedOffer(
                offerId = rs.getString("offer_id"),
                productId = rs.getString("product_id"),
                country = rs.getString("country"),
                modifiedAt = rs.getTimestamp("modified_at").toInstant(),
            )
        }
    }
}
