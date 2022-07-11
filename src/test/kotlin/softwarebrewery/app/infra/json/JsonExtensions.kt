package softwarebrewery.app.infra.json

import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.datatype.jsr310.*
import com.fasterxml.jackson.module.kotlin.*
import com.google.protobuf.*
import java.nio.charset.StandardCharsets.*

inline fun <reified T> ByteString.deserializeJsonAs(): T {
    val jackson = jacksonObjectMapper()
        .registerModule(JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)!!
    val jsonBytes = asReadOnlyByteBuffer()
    return jackson.readValue(UTF_8.decode(jsonBytes).toString())
}
