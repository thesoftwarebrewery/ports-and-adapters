package softwarebrewery.demo.technical

import com.google.cloud.spring.pubsub.core.*
import com.google.protobuf.*
import com.google.pubsub.v1.*
import java.util.concurrent.TimeUnit.*

// wait to observe publication success or failure forcing tests to fail fast in case e.g. a topic is not found
fun PubSubOperations.publishSync(topic: String, message: PubsubMessage) {
    publish(topic, message).get(3, SECONDS)
}

fun aPubSubMessage(
    data: String,
    attributes: Map<String, String> = emptyMap(),
) = aPubSubMessage(
    attributes = attributes,
    data = ByteString.copyFromUtf8(data),
)

fun aPubSubMessage(
    data: ByteString,
    attributes: Map<String, String> = emptyMap(),
) = PubsubMessage.newBuilder()
    .putAllAttributes(attributes)
    .setData(data)
    .build()!!
