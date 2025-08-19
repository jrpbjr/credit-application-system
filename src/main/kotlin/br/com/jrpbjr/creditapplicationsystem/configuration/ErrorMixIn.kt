package br.com.jrpbjr.creditapplicationsystem.configuration

import br.com.jrpbjr.creditapplicationsystem.generated.application.web.dto.Error
import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime

interface ErrorMixIn {
    @get:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val timestamp: LocalDateTime?
}

@Configuration
class ErrorTimestampMixinConfig {

    // Substitui o bean que retornava ObjectMapper (que causava ciclo)
    @Bean
    fun errorTimestampMixinCustomizer(): Jackson2ObjectMapperBuilderCustomizer =
        Jackson2ObjectMapperBuilderCustomizer { builder ->
            builder.mixIn(Error::class.java, ErrorMixIn::class.java)
        }
}