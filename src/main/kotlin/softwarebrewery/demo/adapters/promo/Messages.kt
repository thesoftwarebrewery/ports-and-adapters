package softwarebrewery.demo.adapters.promo

import com.fasterxml.jackson.databind.SerializationFeature.*
import com.fasterxml.jackson.datatype.jsr310.*
import com.fasterxml.jackson.module.kotlin.*
import java.nio.*
import java.nio.charset.StandardCharsets.*
import java.time.*

class MessageAttributes {

    companion object {
        const val CHANGE_TYPE = "change_type"
        const val CHANGE_TYPE_CREATE = "CREATE"
        const val CHANGE_TYPE_DELETE = "DELETE"
    }
}

data class PromotionMessage(
    val timestampUtc: Instant?,
    val promotionId: String?,
    val productId: String?,
    val country: String?,
) {

    fun toJson(): String = jackson.writeValueAsString(this)

    companion object {

        val jackson = jacksonObjectMapper()
            .registerModule(JavaTimeModule())
            .disable(WRITE_DATES_AS_TIMESTAMPS)!!

        fun fromJsonBytes(bytes: ByteBuffer): PromotionMessage = jackson.readValue(UTF_8.decode(bytes).toString())

    }
}
