package br.com.AEP.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfiguration {

    @Bean
    OpenAPI alimentoOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestão e Redistribuição de Alimentos Excedentes")
                        .description("API REST da PoC de Gestão e Redistribuição de Alimentos Excedentes")
                        .version("v1"));
    }
}