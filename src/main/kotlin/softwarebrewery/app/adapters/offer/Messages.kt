package softwarebrewery.app.adapters.offer

import com.fasterxml.jackson.databind.SerializationFeature.*
import com.fasterxml.jackson.datatype.jsr310.*
import com.fasterxml.jackson.module.kotlin.*
import java.nio.*
import java.nio.charset.StandardCharsets.*
import java.time.*

private val jackson = jacksonObjectMapper()
    .registerModule(JavaTimeModule())
    .disable(WRITE_DATES_AS_TIMESTAMPS)!!

class MessageAttributes {

    companion object {
        const val EVENT_TYPE = "event_type"
        const val EVENT_TYPE_OFFER_CREATED = "OFFER_CREATED"
        const val EVENT_TYPE_OFFER_PROMOTED = "OFFER_PROMOTED"
    }
}

data class ExternalOfferCreated(
    val createdAt: Instant?,
    val offerId: String?,
    val productId: String?,
    val country: String?,
) {

    fun toJson(): String = jackson.writeValueAsString(this)

    companion object {
        fun fromJsonBytes(bytes: ByteBuffer): ExternalOfferCreated = jackson.readValue(UTF_8.decode(bytes).toString())
    }
}

data class ExternalOfferPromoted(
    val publishedAt: Instant?,
    val promotionId: String?,
    val country: String?,
    val offerId: String?,
) {
    fun toJson(): String = jackson.writeValueAsString(this)
}
