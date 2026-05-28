package it.apuliadigital.comicstore.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// configurazione di swagger/openapi
// senza questa classe swagger funzionerebbe lo stesso ma con info generiche
@Configuration
public class OpenApiConfig {

    // creo il bean OpenAPI con le informazioni del progetto
    @Bean
    public OpenAPI comicStoreOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Fumetteria API")
                        .description("API REST per la gestione del catalogo fumetti: ricerca, aggiornamento scorte e gestione del magazzino.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("ApuliaDigital")
                                .email("info@apuliadigital.it"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
