package app.aerospike

import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter
import java.math.BigDecimal

object AerospikeConverters {
    val convertersToRegister: List<Any>
        get() {
            return listOf(
                BigDecimalToStringConverter,
                StringToBigDecimalConverter
            )
        }

    @WritingConverter
    object BigDecimalToStringConverter : Converter<BigDecimal, String> {
        override fun convert(source: BigDecimal): String {
            return source.toString()
        }
    }

    @ReadingConverter
    object StringToBigDecimalConverter : Converter<String, BigDecimal> {
        override fun convert(source: String): BigDecimal {
            return BigDecimal(source)
        }
    }
}

