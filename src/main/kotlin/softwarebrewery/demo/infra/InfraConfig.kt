package softwarebrewery.demo.infra

import com.google.api.gax.core.*
import com.google.cloud.spring.pubsub.core.*
import org.springframework.context.annotation.*
import softwarebrewery.demo.infra.pubsub.*
import java.time.*
import java.time.Instant.*

@Configuration
class InfraConfig {

    @Bean
    fun systemClock(): () -> Instant = { now() }

    @Bean
    fun credentialsProvider() = NoCredentialsProvider()

    @Bean
    fun pubSubSubscriberFactory(pubSubOps: PubSubOperations) = PubSubSubscriber.Factory(
        pubSubOps = pubSubOps,
        handleFailure = LogErrorAndDropMessage,
    )
}
