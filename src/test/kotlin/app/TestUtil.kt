package app

import app.testcontainers.AerospikeContainer
import mu.KLogger
import org.springframework.boot.test.util.TestPropertyValues

fun aerospikeContainerInit(
    logger: KLogger,
    container: AerospikeContainer
): TestPropertyValues {
    container.start()
    val host = "${container.containerIpAddress}:${container.getPort()}"

    logger.debug { "Aerospike address: $host" }

    return TestPropertyValues.of(
        "aerospike.hosts=$host"
    )
}

