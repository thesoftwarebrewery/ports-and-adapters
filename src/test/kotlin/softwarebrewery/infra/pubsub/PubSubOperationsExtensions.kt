package softwarebrewery.infra.pubsub

import com.google.cloud.spring.pubsub.core.*
import com.google.protobuf.*
import com.google.pubsub.v1.*
import org.assertj.core.api.Assertions.*
import org.awaitility.Awaitility.*
import org.awaitility.core.*
import java.util.concurrent.TimeUnit.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

fun PubSubOperations.publishSync(topic: String, message: PubsubMessage, timeout: Duration = 3.seconds) {
    // wait to observe publication success or propagate failure when it occurs
    // this ensures tests fail fast in case for example a topic is not found
    publish(topic, message)
        .get(timeout.inWholeMilliseconds, MILLISECONDS)
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
) = aPubSubMessage(attributes, ByteString.copyFromUtf8(data))

fun aPubSubMessage(
    attributes: Map<String, String> = emptyMap(),
    data: ByteString,
) = PubsubMessage.newBuilder()
    .putAllAttributes(attributes)
    .setData(data)
    .build()!!
