package it.apuliadigital.comicstore.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

// DTO semplice per passare solo la quantità
// lo uso sia per stock che per sell così non duplico il codice
@Data
public class QuantityRequest {

    // minimo 1 perché non ha senso vendere o aggiungere 0 copie
    @NotNull(message = "La quantità è obbligatoria")
    @Min(value = 1, message = "La quantità deve essere almeno 1")
    private Integer quantity;
}
