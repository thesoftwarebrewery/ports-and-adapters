package softwarebrewery.demo.adapters.application

import org.springframework.dao.*
import org.springframework.jdbc.core.*
import org.springframework.jdbc.core.namedparam.*
import softwarebrewery.demo.domain.model.*
import softwarebrewery.demo.domain.ports.*
import java.sql.*
import java.time.*

class JdbcPromoRepo(
    private val db: NamedParameterJdbcTemplate,
    private val clock: () -> Instant,
) : PromoRepo {

    override fun new(promotionId: PromotionId, productId: ProductId, country: Country) =
        VersionedPromo(promotionId, productId, country, modifiedAt = null)

    override fun insert(promo: Promo): Modified<Promo> {
        val versioned = promo as VersionedPromo

        val query = """
            insert into promos (promotion_id, product_id, country, created_at, modified_at)
            values (:promotion_id, :product_id, :country, :created_at, :modified_at)
        """.trimIndent()
        val txTime = clock()
        val params = mapOf(
            "promotion_id" to versioned.promotionId,
            "product_id" to versioned.productId,
            "country" to versioned.country,
            "created_at" to Timestamp.from(txTime),
            "modified_at" to Timestamp.from(txTime),
        )

        db.update(query, params)

        return Modified(it = versioned.copy(modifiedAt = txTime), at = txTime)
    }

    override fun update(promo: Promo): Modified<Promo> {
        val versioned = promo as VersionedPromo

        val query = """
            update offers
                set modified_at = :old_modified_at
            where
                offer_id = :offer_id
                and modified_at = :old_modified_at
                and :new_modified_at > :old_modified_at
        """.trimIndent()
        val txTime = clock()
        val params = mapOf(
            "promotion_id" to versioned.promotionId,
            "old_modified_at" to versioned.modifiedAt!!,
            "new_modified_at" to txTime,
        )

        val affected = db.update(query, params)
        if (affected != 1) {
            throw OptimisticLockingFailureException("promos")
        }
        return Modified(it = versioned.copy(modifiedAt = txTime), at = txTime)
    }

    override fun findByProductId(productId: ProductId): Collection<Promo> {
        val query = """
            select promotion_id, product_id, country, created_at, modified_at
            from promos
            where product_id = :product_id 
        """.trimIndent()
        val params = mapOf("product_id" to productId)
        return db.query(query, params, offerMapper)
    }

    companion object {

        private val offerMapper = RowMapper { rs, _ ->
            VersionedPromo(
                promotionId = rs.getString("promotion_id"),
                productId = rs.getString("product_id"),
                country = rs.getString("country"),
                modifiedAt = rs.getTimestamp("modified_at").toInstant(),
            )
        }
    }
}
