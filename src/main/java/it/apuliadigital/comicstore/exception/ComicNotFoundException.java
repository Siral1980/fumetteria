package it.apuliadigital.comicstore.exception;

// eccezione personalizzata per quando non trovo un fumetto
// estende RuntimeException così non devo metterla nei throws dei metodi
public class ComicNotFoundException extends RuntimeException {
    public ComicNotFoundException(String message) {
        super(message);
    }
}
