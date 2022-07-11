package softwarebrewery.app

import org.springframework.boot.*
import org.springframework.boot.autoconfigure.*

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
