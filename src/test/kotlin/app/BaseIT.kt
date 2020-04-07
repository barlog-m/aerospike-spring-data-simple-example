package app

import app.testcontainers.AerospikeContainer
import mu.KLogging
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(initializers = [BaseIT.Companion.Initializer::class])
@ActiveProfiles("test")
abstract class BaseIT {
    companion object: KLogging() {
        private val aerospike = AerospikeContainer()

        class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
            override fun initialize(applicationContext: ConfigurableApplicationContext) {
                val aerospikeTestPropertyValues = aerospikeContainerInit(logger, aerospike)
                aerospikeTestPropertyValues.applyTo(applicationContext)
            }
        }
    }
}
