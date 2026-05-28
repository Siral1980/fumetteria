package it.apuliadigital.comicstore.exception;

// eccezione per quando provo a vendere più copie di quante ne ho in magazzino
public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String message) {
        super(message);
    }
}
