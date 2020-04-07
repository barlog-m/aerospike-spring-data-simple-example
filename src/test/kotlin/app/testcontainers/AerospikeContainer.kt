package app.testcontainers

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import java.time.Duration

class AerospikeContainer : GenericContainer<AerospikeContainer>(
    "$DEFAULT_IMAGE_NAME:$DEFAULT_TAG"
) {
    companion object {
        private const val DEFAULT_IMAGE_NAME = "aerospike"
        private const val DEFAULT_TAG = "latest"

        const val DEFAULT_AEROSPIKE_PORT = 3000
    }

    init {
        withEnv("NAMESPACE", "app")
        addExposedPort(DEFAULT_AEROSPIKE_PORT)
        this.waitStrategy = Wait
            .forLogMessage(".* data exchange completed .*", 1)
            .withStartupTimeout(Duration.ofSeconds(30))
    }

    fun getPort(): Int = getMappedPort(DEFAULT_AEROSPIKE_PORT)
}
