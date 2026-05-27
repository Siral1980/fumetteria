package it.apuliadigital.comicstore.exceptions;

public class ComicNotFoundException extends RuntimeException {
    public ComicNotFoundException(String message) {
        super(message);
    }

    public ComicNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
