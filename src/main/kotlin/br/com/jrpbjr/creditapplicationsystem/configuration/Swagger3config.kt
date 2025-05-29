package br.com.jrpbjr.creditapplicationsystem.configuration

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.servers.Server
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Swagger3Config {
    @Bean
    fun publicApi(): GroupedOpenApi? {
        return GroupedOpenApi.builder()
            .group("springcreditapplicationsystem-public")
            .pathsToMatch("/api/customers/**", "/api/credits/**")
            .build()
    }

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Credit Application System API")
                    .description("API do Sistema de Análise de Solicitação de Crédito")
                    .version("1.0.0")
                    .contact(
                        Contact()
                            .name("José Roberto P. B. Jr")
                            .email("jrpbjr@gmail.com")
                            .url("https://linkedin.com/in/jose-roberto-paes-barbosa-junior-68193485")

                    )
                    .license(
                        License()
                            .name("Apache 2.0")
                            .url("https://www.apache.org/licenses/LICENSE-2.0")
                    )
            )
            .addServersItem(
                Server()
                    .url("http://localhost:8080")
                    .description("Servidor Local")
            )
    }
}


