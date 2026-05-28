package it.apuliadigital.comicstore.exception;

// eccezione per quando provo ad aggiungere un fumetto con un titolo già esistente
// il titolo è unique quindi non posso avere duplicati
public class ComicAlreadyExistsException extends RuntimeException {
    public ComicAlreadyExistsException(String message) {
        super(message);
    }
}
