package softwarebrewery.demo.domain.ports

import java.time.Instant

data class Modified<T>(
    val value: T,
    val at: Instant,
)
