package softwarebrewery.demo

import org.springframework.boot.*
import org.springframework.boot.autoconfigure.*

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}
