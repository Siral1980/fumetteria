package it.apuliadigital.comicstore.models; // Pacchetto dei model/entity.
 
import jakarta.persistence.*; // Importa le annotazioni JPA: Entity, Table, Id, Column, ecc.
import lombok.Data; // Importa Lombok Data, che crea getter, setter, toString, ecc.
 
@Entity // Dice: questa classe e una tabella del database.
@Table(name = "comic") // Dice che la tabella si chiamera comic.
@Data // Lombok crea automaticamente getter e setter per tutti i campi.
public class Comic { // Classe che rappresenta un fumetto.
 
    @Id // Dice che questo campo e la chiave primaria della tabella.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // L'id viene generato automaticamente dal database.
    private Long id; // Identificativo univoco del fumetto.
 
    @Column(nullable = false, unique = true) // Il titolo non puo essere null e deve essere unico.
        private String title; // Titolo del fumetto.
 
        private String author; // Autore del fumetto.
 
        private Double price; // Prezzo del fumetto.
 
        private String genre; // Genere del fumetto.
 
        private int quantity; // Quantita disponibile in magazzino.
 
    }