package softwarebrewery.demo.domain.ports

import java.time.*
import java.time.Instant.*
import java.time.temporal.*
import kotlin.time.*
import kotlin.time.Duration

class FakeClock : Clock, () -> Instant {

    // zero out 'noisy' units more precise than a second, simplifying timestamp comparison by humans
    private var current = now().truncatedTo(ChronoUnit.HOURS)

    override fun invoke(): Instant = current

    fun advance(duration: Duration): Instant {
        current.plus(duration.toJavaDuration()).also { current = it }
        return current
    }
}
