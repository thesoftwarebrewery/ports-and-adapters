package softwarebrewery.demo.domain.ports

import java.time.*

data class Modified<T>(
    val it: T,
    val at: Instant,
)
