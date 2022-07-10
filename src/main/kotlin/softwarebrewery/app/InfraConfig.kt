package softwarebrewery.app

import com.google.api.gax.core.*
import com.google.cloud.spring.pubsub.core.*
import org.springframework.context.*
import org.springframework.context.annotation.*
import org.springframework.jdbc.core.namedparam.*
import org.springframework.transaction.*
import softwarebrewery.app.infra.pubsub.*
import java.time.*
import java.time.Instant.*
import kotlin.time.Duration.Companion.seconds

@Configuration
class InfraConfig {

    @Bean
    fun systemClock(): () -> Instant = { now() }

    @Bean
    fun credentialsProvider() = NoCredentialsProvider()

    @Bean
    fun newPubSubSubscriber(pubSubOps: PubSubOperations) = NewPubSubSubscriber { subscription, handler ->
        PubSubSubscriber(
            subscriberOps = pubSubOps,
            subscription = subscription,
            handleMessage = handler,
            handleFailure = LogErrorAndDropMessage,
        )
    }

    @Bean
    fun outboxStorage(db: NamedParameterJdbcTemplate, txManager: PlatformTransactionManager) =
        JdbcOutboxStorage(db, txManager)

    @Bean
    fun pubSubOutbox(
        outboxStorage: OutboxStorage,
        eventPublisher: ApplicationEventPublisher,
        publishMessage: PubSubPublisher,
    ) = TransactionalPubSubOutbox(outboxStorage, eventPublisher, publishMessage)

    @Bean
    fun outboxRetryJob(outboxStorage: OutboxStorage, publishMessage: PubSubPublisher) =
        PubSubOutboxRetryJob(
            outboxStorage = outboxStorage,
            publishMessage = publishMessage,
            runInterval = 5.seconds,
            maxMessagesPerBatch = 1000,
            minMessageAge = 30.seconds,
        )

    @Bean
    fun pubSubPublisher(pubSubOps: PubSubOperations) = PubSubPublisher(pubSubOps::publish)

}
