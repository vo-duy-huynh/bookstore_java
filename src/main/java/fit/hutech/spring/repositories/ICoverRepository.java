package fit.hutech.spring.repositories;

import fit.hutech.spring.entities.Book;
import fit.hutech.spring.entities.Cover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICoverRepository extends JpaRepository<Cover, Long> {
    @Query("""
            SELECT c FROM Cover c
            WHERE c.book.id = :id
            """)
    Optional<Cover> getCoverById(Long id);
}
