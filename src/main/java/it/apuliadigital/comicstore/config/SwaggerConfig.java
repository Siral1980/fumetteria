package it.apuliadigital.comicstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI comicStoreOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Comic Store API")
                        .description("API per la gestione di fumetti, magazzino e vendite.")
                        .version("1.0.0"));
    }
}