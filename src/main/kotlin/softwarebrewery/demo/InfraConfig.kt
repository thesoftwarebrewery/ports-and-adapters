package softwarebrewery.demo

import com.google.api.gax.core.*
import com.google.cloud.spring.pubsub.core.*
import mu.*
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.*
import org.springframework.jdbc.core.namedparam.*
import org.springframework.transaction.*
import softwarebrewery.demo.infra.pubsub.*
import java.time.*
import java.time.Instant.*

private val log = KotlinLogging.logger {  }

@Configuration
class InfraConfig {

    @Bean
    fun systemClock(): () -> Instant = { now() }

    @Bean
    fun credentialsProvider() = NoCredentialsProvider()

    @Bean
    fun newPubSubSubscriber(pubSubOps: PubSubOperations): NewPubSubSubscriber = { subscription, handler ->
        PubSubSubscriber(
            subscriberOps = pubSubOps,
            subscription = subscription,
            handleMessage = handler,
            handleFailure = LogErrorAndDropMessage,
        )
    }

    @Bean
    fun outboxStorage(db: NamedParameterJdbcTemplate, txManager: PlatformTransactionManager): OutboxStorage =
        JdbcOutboxStorage(db, txManager)

    @Bean
    fun pubSubOutbox(
        outboxStorage: OutboxStorage,
        eventPublisher: ApplicationEventPublisher,
        publishMessage: PubSubPublisher
    ) = TransactionalPubSubOutbox(
        outboxStorage = outboxStorage,
        eventPublisher = eventPublisher,
        publishMessage = publishMessage,
    )

    @Bean
    fun pubSubPublisher(pubSubOps: PubSubOperations) = PubSubPublisher { topic, message ->
        log.info { "publishing to '$topic' message '$message'" }
        pubSubOps.publish(topic, message)
    }
}
