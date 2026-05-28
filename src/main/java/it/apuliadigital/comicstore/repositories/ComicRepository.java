package it.apuliadigital.comicstore.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import it.apuliadigital.comicstore.models.Comic;

@Repository
public interface ComicRepository extends JpaRepository<Comic, Long> {
    Optional<Comic> findByTitle(String title);
    
    @Query("SELECT c FROM Comic c WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :filter, '%')) OR LOWER(c.author) LIKE LOWER(CONCAT('%', :filter, '%'))")
    List<Comic> findByFilter(String filter);
    
    List<Comic> findByOutOfStockTrue();
    
    @Query("SELECT c.title FROM Comic c WHERE c.outOfStock = true")
    List<String> findOutOfStockNames();
}
