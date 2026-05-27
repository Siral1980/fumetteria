package it.apuliadigital.comicstore.repositories;

import it.apuliadigital.comicstore.models.Sell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellRepository extends JpaRepository<Sell, Integer> {

}
