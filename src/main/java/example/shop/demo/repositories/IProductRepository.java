package example.shop.demo.repositories;

import example.shop.demo.entities.Product;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepository extends PagingAndSortingRepository<Product, Long>, JpaRepository<Product, Long> {
    @Query("""
            SELECT b FROM Product b
            WHERE b.title LIKE %?1%
            OR b.author LIKE %?1%
            OR b.category.name LIKE %?1%
            OR CAST(b.price AS string) LIKE %?1%
            """)
    List<Product> searchProduct(String keyword);

    default List<Product> findAllProducts(Integer pageNo, Integer pageSize, String sortBy) {
        return findAll(PageRequest.of(pageNo, pageSize, Sort.by(sortBy)))
                    .getContent();
    }
    @Query("""
            DELETE FROM Cover c
            WHERE c.product.id = ?1
            """)
    void deleteCoverByProductId(Long id);

}