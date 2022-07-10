package softwarebrewery.app.domain.ports

import softwarebrewery.app.domain.*
import java.time.*

@SecondaryPort
fun interface Clock {
    operator fun invoke(): Instant
}
