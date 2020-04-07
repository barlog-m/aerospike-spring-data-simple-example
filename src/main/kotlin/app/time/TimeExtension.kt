package app.time

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

fun LocalDateTime.truncate(): LocalDateTime = this.truncatedTo(ChronoUnit.MILLIS)
