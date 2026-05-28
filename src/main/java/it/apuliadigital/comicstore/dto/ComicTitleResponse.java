package it.apuliadigital.comicstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

// DTO di risposta che contiene solo il titolo
// serve per findOutOfStock che deve restituire solo i nomi, nessun altro dato
@Data
@AllArgsConstructor
public class ComicTitleResponse {
    private String title;
}
