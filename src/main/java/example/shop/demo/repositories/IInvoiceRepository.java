package example.shop.demo.repositories;

import example.shop.demo.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IInvoiceRepository extends JpaRepository<Invoice, Long>{
    @Query("SELECT i FROM Invoice i WHERE i.user.username = ?1")

    Invoice getInvoicesByUser(String username);
}
