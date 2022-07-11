package softwarebrewery.infra.pubsub

import com.google.pubsub.v1.*
import org.springframework.util.concurrent.*
import softwarebrewery.app.domain.*

fun interface PubSubPublisher {

    operator fun invoke(topic: String, message: PubsubMessage) : ListenableFuture<String>

    fun handle(x: DomainApi) {

    }

}
