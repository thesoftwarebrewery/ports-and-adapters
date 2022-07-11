package softwarebrewery.app.adapters.application

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.*
import org.springframework.boot.test.autoconfigure.data.jdbc.*
import org.springframework.boot.test.autoconfigure.jdbc.*
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*
import org.springframework.jdbc.core.*
import org.springframework.jdbc.core.namedparam.*
import org.springframework.test.context.*
import softwarebrewery.app.domain.ports.*
import softwarebrewery.infra.*
import java.sql.*
import kotlin.time.Duration.Companion.seconds

@ActiveProfiles("it")
@DataJdbcTest
@AutoConfigureTestDatabase(replace = NONE)
class JdbcOfferRepoIT(
    @Autowired private val db: NamedParameterJdbcTemplate,
) {

    private val clock = FakeClock()
    private val repo = JdbcOfferRepo(db, clock)

    @Test
    fun `inserts a new offer`() {
        val new = repo.new(
            offerId = namedRandom("offer"),
            productId = namedRandom("product"),
            country = namedRandom("country"),
        )

        val persisted = repo.insert(new)

        assertThat(db.findAll()).containsExactly(
            mapOf(
                "offer_id" to new.offerId,
                "product_id" to new.productId,
                "country" to new.country,
                "created_at" to Timestamp.from(clock()),
                "modified_at" to Timestamp.from(clock()),
            )
        )
        assertThat(persisted).isEqualTo(
            Modified(
                it = VersionedOffer(
                    offerId = new.offerId,
                    productId = new.productId,
                    country = new.country,
                    modifiedAt = clock(),
                ),
                at = clock(),
            )
        )
        assertThat(persisted.it).isEqualTo(
            VersionedOffer(
                offerId = new.offerId,
                productId = new.productId,
                country = new.country,
                modifiedAt = clock(),
            )
        )
        assertThat(persisted.at).isEqualTo(clock())
    }

    @Test
    fun `finds existing offers by product id`() {
        val existing = mapOf(
            "offer_id" to namedRandom("offer"),
            "product_id" to namedRandom("product"),
            "country" to namedRandom("country"),
            "created_at" to Timestamp.from(clock()),
            "modified_at" to Timestamp.from(clock().plusSeconds(1)),
        )
        db.insertOffer(existing)

        val offers = repo.findByProductId(existing["product_id"] as String)

        assertThat(offers).containsExactly(
            VersionedOffer(
                offerId = existing["offer_id"] as String,
                productId = existing["product_id"] as String,
                country = existing["country"] as String,
                modifiedAt = clock().plusSeconds(1),
            )
        )
    }

    @Test
    fun `updates an existing offer`() {
        val existing = mapOf(
            "offer_id" to namedRandom("offer"),
            "product_id" to namedRandom("product"),
            "country" to namedRandom("country"),
            "created_at" to Timestamp.from(clock()),
            "modified_at" to Timestamp.from(clock()),
        )
        db.insertOffer(existing)
        val update = repo.findByProductId(existing["product_id"] as String).single().clone(
            country = "NEW-COUNTRY",
            productId = "NEW-PRODUCT",
        )
        val updateAt = clock.forward(2.seconds)

        val persisted = repo.update(update)

        assertThat(db.findAll()).containsExactly(
            mapOf(
                "offer_id" to persisted.it.offerId,
                "product_id" to "NEW-PRODUCT",
                "country" to "NEW-COUNTRY",
                "created_at" to existing["created_at"] as Timestamp,
                "modified_at" to Timestamp.from(updateAt),
            )
        )
        assertThat(persisted).isEqualTo(
            Modified(
                it = VersionedOffer(
                    offerId = existing["offer_id"] as String,
                    productId = "NEW-PRODUCT",
                    country = "NEW-COUNTRY",
                    modifiedAt = updateAt,
                ),
                at = updateAt,
            )
        )
    }

    fun NamedParameterJdbcTemplate.findAll(): List<Map<String, Any>> =
        query("select * from offers", emptyMap<String, String>(), ColumnMapRowMapper())

    fun NamedParameterJdbcTemplate.insertOffer(params: Map<String, Any>) {
        val query = """
            insert into offers (offer_id, product_id, country, created_at, modified_at)
            values (:offer_id, :product_id, :country, :created_at, :modified_at)
        """
        update(query, params)
    }
}
