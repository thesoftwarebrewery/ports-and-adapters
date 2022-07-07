package softwarebrewery.demo.adapters.offers

import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.datatype.jsr310.*
import com.fasterxml.jackson.module.kotlin.*
import java.nio.*
import java.nio.charset.StandardCharsets.*
import java.time.*

class MessageAttributes {

    companion object {
        const val EVENT_TYPE = "event_type"
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

        val jackson = jacksonObjectMapper()
            .registerModule(JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)!!

        fun fromJsonBytes(bytes: ByteBuffer): ExternalOfferCreated = jackson.readValue(UTF_8.decode(bytes).toString())

    }
}
