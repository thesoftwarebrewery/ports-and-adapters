package softwarebrewery.demo.domain.ports

import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit

class FakeClock : Clock {

    private var current = Instant.now().truncatedTo(ChronoUnit.DAYS) // zero out 'detailed' time for easier readability

    override fun invoke(): Instant = current

    fun advance(): Instant {
        current.plus(Duration.ofSeconds(1)).also { current = it }
        return current
    }
}
