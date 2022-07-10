package softwarebrewery.demo.infra.pubsub

import com.google.cloud.spring.pubsub.core.*
import com.google.protobuf.*
import com.google.pubsub.v1.*
import org.assertj.core.api.Assertions.*
import org.awaitility.Awaitility.*
import org.awaitility.core.*
import java.util.concurrent.TimeUnit.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

// wait to observe publication success or failure forcing tests to fail fast in case e.g. a topic is not found
fun PubSubOperations.publishSync(topic: String, message: PubsubMessage, timeout: Duration = 3.seconds) {
    publish(topic, message).get(timeout.inWholeMilliseconds, MILLISECONDS)
    // todo: allow message handling to commit before next message to prevent missed links in concurrent tx
}

fun PubSubOperations.assertReceived(
    subscription: String,
    assert: (PubsubMessage) -> Unit,
) {
    await()
        .conditionEvaluationListener(ConditionEvaluationLogger())
        .untilAsserted {
            val next = pullNext(subscription)
            assertThat(next).isNotNull
            assert(next)
        }
}

fun aPubSubMessage(
    attributes: Map<String, String> = emptyMap(),
    data: String,
) = aPubSubMessage(
    attributes = attributes,
    data = ByteString.copyFromUtf8(data),
)

fun aPubSubMessage(
    attributes: Map<String, String> = emptyMap(),
    data: ByteString,
) = PubsubMessage.newBuilder()
    .putAllAttributes(attributes)
    .setData(data)
    .build()!!
