package app.time

import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class RealTimeService : TimeService {
    override fun currentDateTime() = LocalDateTime.now().truncate()
}
