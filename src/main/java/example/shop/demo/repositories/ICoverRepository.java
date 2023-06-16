package example.shop.demo.repositories;

import example.shop.demo.entities.Cover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICoverRepository extends JpaRepository<Cover, Long> {
    @Query("""
            SELECT c FROM Cover c
            WHERE c.product.id = :id
            """)
    Optional<Cover> getCoverById(Long id);

    @Query("""
            SELECT c FROM Cover c
            WHERE c.product.id = :id
            """)
    List<Cover> getCoverByProductId(Long id);
}
