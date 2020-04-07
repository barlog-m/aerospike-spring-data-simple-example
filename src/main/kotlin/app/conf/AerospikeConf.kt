package app.conf

import app.aerospike.AerospikeConverters
import app.aerospike.AerospikeConverters.BigDecimalToStringConverter
import app.aerospike.AerospikeConverters.StringToBigDecimalConverter
import app.props.AerospikeProps
import app.repository.TrxRepository
import com.aerospike.client.async.NioEventLoops
import com.aerospike.client.policy.ClientPolicy
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.data.aerospike.config.AbstractReactiveAerospikeDataConfiguration
import org.springframework.data.aerospike.convert.CustomConversions
import org.springframework.data.aerospike.repository.config.EnableReactiveAerospikeRepositories

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(AerospikeProps::class)
@EnableReactiveAerospikeRepositories(basePackageClasses = [TrxRepository::class])
open class AerospikeConf(
    private val props: AerospikeProps
) : AbstractReactiveAerospikeDataConfiguration() {
    override fun nameSpace() = props.namespace

    override fun getHosts() = props.hosts().toMutableList()

    override fun eventLoops() = NioEventLoops()

    override fun getClientPolicy(): ClientPolicy {
        return super.getClientPolicy().apply {
            failIfNotConnected = true
            timeout = 2000
        }
    }

    override fun customConverters(): List<Any> {
        return AerospikeConverters.convertersToRegister
    }
}
