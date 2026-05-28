package it.apuliadigital.comicstore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

// questa classe gestisce tutte le eccezioni dell'applicazione in un posto solo
// senza questo ogni controller dovrebbe gestirle da solo, molto scomodo
// @RestControllerAdvice intercetta le eccezioni prima che arrivino al client
@RestControllerAdvice
public class GlobalExceptionHandler {

    // struttura standard per le risposte di errore
    // uso record perché è più corto di una classe normale con getter e costruttore
    private record ErrorResponse(int status, String error, String message, LocalDateTime timestamp) {}

    // metodo helper per non ripetere il codice di costruzione della risposta
    private ResponseEntity<ErrorResponse> build(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(new ErrorResponse(status.value(), status.getReasonPhrase(), message, LocalDateTime.now()));
    }

    // fumetto non trovato -> 404 Not Found
    @ExceptionHandler(ComicNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ComicNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // fumetto già esistente -> 409 Conflict
    @ExceptionHandler(ComicAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleConflict(ComicAlreadyExistsException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage());
    }

    // scorte insufficienti -> 422 Unprocessable Entity
    // ho scelto 422 perché la richiesta è valida ma non posso elaborarla per mancanza di stock
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientStock(InsufficientStockException ex) {
        return build(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    // errori di validazione (@NotBlank, @Positive ecc) -> 400 Bad Request
    // qui restituisco i campi sbagliati così il client sa cosa correggere
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fe.getField(), fe.getDefaultMessage());
        }
        Map<String, Object> body = new HashMap<>();
        body.put("status", 400);
        body.put("error", "Validation Failed");
        body.put("fields", fieldErrors);
        body.put("timestamp", LocalDateTime.now());
        return ResponseEntity.badRequest().body(body);
    }

    // catch-all per qualsiasi altra eccezione non prevista -> 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Errore interno: " + ex.getMessage());
    }
}
