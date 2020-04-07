package app.service

import app.model.Trx
import app.time.TimeService
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.UUID
import java.util.concurrent.ThreadLocalRandom

@Service
class TrxFactoryService(
    private val timeService: TimeService
) {
    fun create() = Trx(
        id = UUID.randomUUID(),
        timestamp = timeService.currentDateTime(),
        value = BigDecimal("${randomValue()}.${randomCents()}")
    )

    private fun randomCents() = random().nextInt(99)
    private fun randomValue() = random().nextInt(Int.MAX_VALUE)
    private fun random() = ThreadLocalRandom.current()
}

