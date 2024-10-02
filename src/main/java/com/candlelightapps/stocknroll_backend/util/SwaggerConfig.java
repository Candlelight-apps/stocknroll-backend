package com.candlelightapps.stocknroll_backend.util;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("StocknRoll API Documentation")
                        .version("1.0")
                        .description("API documentation for StocknRoll Spring Boot application"));
    }
}
