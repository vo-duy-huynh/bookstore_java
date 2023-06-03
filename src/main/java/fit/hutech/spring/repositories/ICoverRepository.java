package fit.hutech.spring.repositories;

import fit.hutech.spring.entities.Cover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICoverRepository extends JpaRepository<Cover, Long> {
}
