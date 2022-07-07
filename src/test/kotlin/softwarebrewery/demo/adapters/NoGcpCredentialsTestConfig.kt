package softwarebrewery.demo.adapters

import com.google.api.gax.core.*
import org.springframework.context.annotation.*

@Profile("default", "it")
@Configuration
class NoGcpCredentialsTestConfig {

    @Bean
    fun credentialsProvider(): CredentialsProvider = NoCredentialsProvider()

}
