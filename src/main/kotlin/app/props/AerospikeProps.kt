package app.props

import com.aerospike.client.Host
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.bind.DefaultValue

@ConstructorBinding
@ConfigurationProperties("aerospike")
data class AerospikeProps(
    @DefaultValue("127.0.0.1:3000")
    val hosts: String,

    @DefaultValue("app")
    val namespace: String
) {
    fun hosts(): Array<Host> = Host.parseHosts(hosts, 3000)
}
