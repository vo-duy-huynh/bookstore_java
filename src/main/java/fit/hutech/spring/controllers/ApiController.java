package fit.hutech.spring.controllers;

import fit.hutech.spring.entities.Book;
import fit.hutech.spring.services.BookService;
import fit.hutech.spring.services.CartService;
import fit.hutech.spring.services.CategoryService;
import fit.hutech.spring.viewmodels.BookGetVm;
import fit.hutech.spring.viewmodels.BookPostVm;
import fit.hutech.spring.viewmodels.CoversBookVm;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ApiController {
    private final BookService bookService;

    private final CategoryService categoryService;
    private final CartService cartService;

    @GetMapping("/books")
    public ResponseEntity<List<BookGetVm>> getAllBooks(Integer pageNo, Integer pageSize, String sortBy) {
        return ResponseEntity.ok(bookService.getAllBooks(
                pageNo == null ? 0 : pageNo,
                pageSize == null ? 20 : pageSize,
                sortBy == null ? "id" : sortBy)
                .stream()
                .map(BookGetVm::from)
                .toList());
    }

    @GetMapping("/books/id/{id}")
    public ResponseEntity<BookGetVm> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id)
                .map(BookGetVm::from)
                .orElse(null));
    }
    //covers is string
    @GetMapping("/covers/book/{id}")
    public ResponseEntity<List<CoversBookVm>> getCoversByBookId(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getCoverByBookId(id)
                .stream()
                .map(CoversBookVm::from)
                .toList());
    }
    @PostMapping("/books")
    public ResponseEntity<Void> createBook(@RequestBody @NotNull BookPostVm bookPostVm) {
        bookService.addBook(Book.builder()
                .title(bookPostVm.title())
                .author(bookPostVm.author())
                .price(bookPostVm.price())
                .category(categoryService.getCategoryById(bookPostVm.categoryId()).orElse(null))
                .build());
        return ResponseEntity.ok().build();
    }
    @GetMapping("/cart/add/{id}")
    public ResponseEntity<Void> addBookToCart(@PathVariable Long id) {
        bookService.addBookToCart(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/cart/item-count")
    @ResponseBody
    public Map<String, Integer> getCartItemCount(HttpSession session) {
        int itemCount = cartService.getSumItem(session);
        Map<String, Integer> response = new HashMap<>();
        response.put("count", itemCount);
        return response;
    }
    @PutMapping("/books")
    public ResponseEntity<Void> updateBook(@RequestBody @NotNull BookPostVm bookPostVm) {
        bookService.updateBook(Book.builder()
                .title(bookPostVm.title())
                .author(bookPostVm.author())
                .price(bookPostVm.price())
                .category(categoryService.getCategoryById(bookPostVm.categoryId()).orElse(null))
                .build());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/books/search")
    public ResponseEntity<List<BookGetVm>> searchBooks(String keyword) {
        return ResponseEntity.ok(bookService.searchBook(keyword)
                .stream()
                .map(BookGetVm::from)
                .toList());
    }
    @GetMapping("/books/{productId}/details")
    @ResponseBody
    public Book getProductDetails(@PathVariable Long productId) {
        // Retrieve the product details based on the provided product ID
        Book book = bookService.getBookByIdAll(productId);
        return book;
    }
}
