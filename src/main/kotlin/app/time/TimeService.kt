package app.time

import java.time.LocalDateTime

interface TimeService {
    fun currentDateTime(): LocalDateTime
}
