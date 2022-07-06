package softwarebrewery.demo.domain.ports

import softwarebrewery.demo.domain.*
import java.time.*

@SecondaryPort
fun interface Clock {
    operator fun invoke(): Instant
}
