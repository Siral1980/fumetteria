package it.apuliadigital.comicstore.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.apuliadigital.comicstore.SellDTO;
import it.apuliadigital.comicstore.models.Sell;
import it.apuliadigital.comicstore.repositories.SellRepository;

@Service
public class SellService {
    @Autowired
    private SellRepository sellRepository;

    private SellDTO convertToDTO(Sell sell) {
        return new SellDTO(
            sell.getComic().getTitle(),
            sell.getQuantity(),
            sell.getSaleDate(),
            sell.getTotalPrice()
        );
    }
    public List<SellDTO> findByRange(LocalDateTime start, LocalDateTime end) {
        if(start.isAfter(end)){
            throw new IllegalArgumentException
            ("La data di inizio deve essere precedente alla data di fine.");
        }
        return sellRepository.findBySaleDateBetween(start, end)
        .stream()
        .map(this::convertToDTO)
        .toList();
        }
        public List<SellDTO> findByAmountGreaterThan(double amount) {
            if(amount < 0){
                throw new IllegalArgumentException
                ("L'importo deve essere maggiore o uguale a zero.");
            }
            return sellRepository.findByTotalPriceGreaterThan(amount)
            .stream()
            .map(this::convertToDTO)
            .toList();
        }
    }
