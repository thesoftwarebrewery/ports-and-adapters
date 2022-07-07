package softwarebrewery.demo.adapters

import com.google.api.gax.core.*
import org.springframework.context.annotation.*

@Configuration
class NoGcpCredentialsTestConfig {

    @Bean
    fun noCredentialsProvider() : CredentialsProvider = NoCredentialsProvider()

}
