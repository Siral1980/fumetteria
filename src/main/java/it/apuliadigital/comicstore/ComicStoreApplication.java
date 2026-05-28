package it.apuliadigital.comicstore;

import it.apuliadigital.comicstore.services.ComicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ComicStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComicStoreApplication.class, args);
	}

}
