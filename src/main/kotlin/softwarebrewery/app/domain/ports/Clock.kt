package softwarebrewery.app.domain.ports

import softwarebrewery.app.domain.*
import java.time.*

@OutboundPort
fun interface Clock {
    operator fun invoke(): Instant
}
