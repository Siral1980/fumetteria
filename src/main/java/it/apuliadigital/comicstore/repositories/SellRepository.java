package it.apuliadigital.comicstore.repositories;

import it.apuliadigital.comicstore.models.Sell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SellRepository extends JpaRepository<Sell, Long> {
    
    /**
     * Find sales within a date range
     */
    @Query("SELECT s FROM Sell s WHERE s.sellingDate >= :startDate AND s.sellingDate <= :endDate")
    List<Sell> findSalesByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    /**
     * Find sales with amount greater than a specified value
     */
    @Query("SELECT s FROM Sell s WHERE s.totalAmount > :amount")
    List<Sell> findSalesByAmountGreaterThan(@Param("amount") BigDecimal amount);
}
