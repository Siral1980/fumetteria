package it.apuliadigital.comicstore.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    // Costruisce un'eccezione con il messaggio che sarà mostrato quando la risorsa non esiste
    public ResourceNotFoundException(String message) {
        super(message);
    }
}