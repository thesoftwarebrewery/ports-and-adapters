package softwarebrewery.demo.adapters.promo

import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.datatype.jsr310.*
import com.fasterxml.jackson.module.kotlin.*
import java.time.*

// owned by promo
data class PromotionMessage(
    val timestampUtc: Instant?,
    val changeType: String?,
    val productId: Int?,
    val countries: Set<String>,
) {

    fun toJson(): String = jackson.writeValueAsString(this)

    companion object {
        val jackson = jacksonObjectMapper()
            .registerModule(JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)!!
    }
}

enum class Country { BE, NL }
enum class ChangeType { CREATE, DELETE }
