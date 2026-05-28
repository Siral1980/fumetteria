package it.apuliadigital.comicstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// questa annotation fa partire tutto spring boot, senza non funziona niente
@SpringBootApplication
public class ComicStoreApplication {

    // metodo main, punto di ingresso dell'applicazione
    public static void main(String[] args) {
        SpringApplication.run(ComicStoreApplication.class, args);
    }
}
