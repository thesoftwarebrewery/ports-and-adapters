package softwarebrewery.demo.domain.ports

import java.time.Instant

typealias Clock = () -> Instant
