package app.model

import org.springframework.data.aerospike.mapping.Document
import org.springframework.data.annotation.Id
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Document
data class Trx(
    @Id
    val id: UUID,
    val timestamp: LocalDateTime,
    val value: BigDecimal
)
