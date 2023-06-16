package example.shop.demo.controllers;

import example.shop.demo.entities.Product;
import example.shop.demo.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class ProductApiController {

    @Autowired
    private ProductService bookRepository;

    public ProductApiController(ProductService bookRepository) {
        this.bookRepository = bookRepository;
    }

//    get BookByID return json
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> book = bookRepository.getBookById(id);
        if (book.isPresent()) {
            return ResponseEntity.ok(book.get());
        }
        return ResponseEntity.notFound().build();
    }

    // Other API methods for books

}
