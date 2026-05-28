package it.apuliadigital.comicstore;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellDTO {
    
    private String comicTitle;
    private int quantity;
    private LocalDateTime saleDate;
    private double totalPrice;
}
