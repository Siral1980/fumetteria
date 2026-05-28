package it.apuliadigital.comicstore.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import it.apuliadigital.comicstore.models.Comic;
import java.util.Optional;
import java.util.List;

@Repository
public interface ComicRepository extends JpaRepository<Comic, Long> { //sei stato cattivissimissimo ihihihihihih
    Optional<Comic> findByTitle(String title);

    List<Comic> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String titleKeyword, String authorKeyword);
    List<Comic> findByOutOfStockTrue();
}
