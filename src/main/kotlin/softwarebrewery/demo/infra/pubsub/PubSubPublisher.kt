package softwarebrewery.demo.infra.pubsub

import com.google.pubsub.v1.*
import org.springframework.util.concurrent.*

fun interface PubSubPublisher {

    operator fun invoke(topic: String, message: PubsubMessage) : ListenableFuture<String>

}
