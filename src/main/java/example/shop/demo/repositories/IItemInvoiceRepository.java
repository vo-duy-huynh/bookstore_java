package example.shop.demo.repositories;

import example.shop.demo.entities.ItemInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IItemInvoiceRepository extends JpaRepository<ItemInvoice, Long>{
    @Query("SELECT i FROM ItemInvoice i WHERE i.invoice.user.username = ?1")
    List<ItemInvoice> getItemInvoicesByUser(String username);
}
