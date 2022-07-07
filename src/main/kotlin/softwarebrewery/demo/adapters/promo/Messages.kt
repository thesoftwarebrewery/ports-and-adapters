package softwarebrewery.demo.adapters.promo

import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.datatype.jsr310.*
import com.fasterxml.jackson.module.kotlin.*
import java.nio.*
import java.nio.charset.StandardCharsets.*
import java.time.*

// owned by promo
data class PromotionMessage(
    val timestampUtc: Instant?,
    val promotionId: Int?,
    val productId: Int?,
    val country: String?,
) {

    fun toJson(): String = jackson.writeValueAsString(this)

    companion object {

        const val CHANGE_TYPE_ATTRIBUTE = "change_type"
        const val CHANGE_TYPE_CREATE = "CREATE"
        const val CHANGE_TYPE_DELETE = "DELETE"

        val jackson = jacksonObjectMapper()
            .registerModule(JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)!!

        fun fromJsonBytes(bytes: ByteBuffer): PromotionMessage = jackson.readValue(UTF_8.decode(bytes).toString())

    }
}

enum class Country { BE, NL }
