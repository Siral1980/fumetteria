package it.apuliadigital.comicstore.models; // Pacchetto dei model.
 
import jakarta.persistence.*; // Importa annotazioni JPA.
import lombok.Data; // Lombok per getter e setter.
 
import java.math.BigDecimal; // Tipo preciso per gestire importi di denaro.
import java.time.LocalDateTime; // Tipo per data e orario insieme.
 
@Entity // Questa classe diventa una tabella del database.
@Table(name = "sell") // La tabella si chiama sell.
@Data // Crea getter e setter automaticamente.
public class Sell {
    @Id // Chiave primaria della vendita.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Id generato dal database.
    private long id; // Id della vendita.
 
    private LocalDateTime sellingDate; // Data e ora in cui e stata fatta la vendita.
 
    private int sellingQuantity; // Quante copie sono state vendute.
 
    private BigDecimal totalAmount; // Prezzo totale: prezzo fumetto * quantita venduta.
 
    @ManyToOne // Molte vendite possono riguardare lo stesso fumetto.
    @JoinColumn(name = "comic_id", nullable = false) // Nel database ci sara una colonna comic_id obbligatoria.
    private Comic comic; // Il fumetto venduto.
}