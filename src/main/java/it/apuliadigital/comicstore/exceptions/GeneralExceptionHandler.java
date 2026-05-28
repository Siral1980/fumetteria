package it.apuliadigital.comicstore.exceptions; // Pacchetto exceptions.
 
import java.time.LocalDateTime; // Per timestamp dell'errore.
import java.util.LinkedHashMap; // Mappa ordinata: mantiene l'ordine dei campi.
import java.util.Map; // Tipo generico per chiave-valore.
import java.util.NoSuchElementException; // Errore quando non troviamo qualcosa.
 
import org.springframework.dao.DataIntegrityViolationException; // Errore per vincoli database, tipo titolo duplicato.
import org.springframework.http.HttpStatus; // Codici HTTP: 404, 400, 409, 500.
import org.springframework.http.ResponseEntity; // Risposta HTTP.
import org.springframework.web.bind.annotation.ExceptionHandler; // Metodo che gestisce un tipo di errore.
import org.springframework.web.bind.annotation.RestControllerAdvice; // Classe che intercetta errori dei controller REST.
 
@RestControllerAdvice // Dice a Spring: questa classe gestisce gli errori di tutti i controller.
public class GeneralExceptionHandler {
 
    @ExceptionHandler(NoSuchElementException.class) // Gestisce fumetto non trovato.
    public ResponseEntity<Map<String, Object>> handleNotFound(NoSuchElementException e) {
        return buildResponse(HttpStatus.NOT_FOUND, e.getMessage()); // 404.
    }
 
    @ExceptionHandler(IllegalArgumentException.class) // Gestisce dati non validi.
    public ResponseEntity<Map<String, Object>> handleBadRequest(IllegalArgumentException e) {
        return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage()); // 400.
    }
 
    @ExceptionHandler(IllegalStateException.class) // Gestisce stato non valido, tipo copie insufficienti.
    public ResponseEntity<Map<String, Object>> handleConflict(IllegalStateException e) {
        return buildResponse(HttpStatus.CONFLICT, e.getMessage()); // 409.
    }
    
 
    @ExceptionHandler(DataIntegrityViolationException.class) // Gestisce vincoli del database.
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        return buildResponse(
                HttpStatus.CONFLICT,
                "Operazione non possibile: esiste gia un fumetto con questo titolo oppure i dati non rispettano i vincoli del database."
        );
    }
 
    @ExceptionHandler(Exception.class) // Gestisce errori generici non previsti.
    public ResponseEntity<Map<String, Object>> handleGenericError(Exception e) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Errore interno del server."); // 500.
    }
 
    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) { // Metodo privato per costruire sempre lo stesso JSON.
 
        Map<String, Object> body = new LinkedHashMap<>(); // Creo una mappa ordinata.
 
        body.put("timestamp", LocalDateTime.now()); // Momento dell'errore.
        body.put("status", status.value()); // Numero HTTP, es. 404.
        body.put("error", status.getReasonPhrase()); // Nome errore, es. Not Found.
        body.put("message", message); // Messaggio spiegato da noi.
 
        return ResponseEntity.status(status).body(body); // Restituisco risposta HTTP con body JSON.
    }
}