package example.shop.demo.controllers;

import example.shop.demo.dto.NewDTO;
import example.shop.demo.dto.ProductDTO;
import example.shop.demo.entities.Category;
import example.shop.demo.entities.Product;
import example.shop.demo.services.ProductService;
import example.shop.demo.services.CartService;
import example.shop.demo.services.CategoryService;
import example.shop.demo.viewmodels.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin("*")
public class ApiController {
    private ProductService productService;
    private CategoryService categoryService;
    private CartService cartService;
    public ApiController(ProductService productService, CategoryService categoryService, CartService cartService) {
        this.categoryService = categoryService;
        this.cartService = cartService;
        this.productService = productService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductGetVm>> getAllProducts() {
        return ResponseEntity.ok(productService.findAllProducts()
                .stream()
                .map(ProductGetVm::from)
                .toList());
    }
    @GetMapping("/products/id/{id}")
    public ResponseEntity<ProductGetVm> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getBookById(id)
                .map(ProductGetVm::from)
                .orElse(null));
    }
    //covers is string
    @GetMapping("/covers/product/{id}")
    public ResponseEntity<List<CoversBookVm>> getCoversByBookId(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getCoverByBookId(id)
                .stream()
                .map(CoversBookVm::from)
                .toList());
    }
    @PostMapping("/products")
    public ResponseEntity<Void> createBook(@RequestBody @NotNull ProductPostVm bookPostVm) {
        productService.addProduct(Product.builder()
                .title(bookPostVm.title())
                .author(bookPostVm.author())
                .price(bookPostVm.price())
                .category(categoryService.getCategoryById(bookPostVm.categoryId()).orElse(null))
                .build());
        return ResponseEntity.ok().build();
    }
    @GetMapping("/cart/add/{id}")
    public ResponseEntity<Void> addBookToCart(@PathVariable Long id) {
        productService.addProductToCart(id);
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
    @PutMapping("/products")
    public ResponseEntity<Void> updateProduct(@RequestBody @NotNull ProductPostVm productPostVm) {
        productService.updateProduct(Product.builder()
                .title(productPostVm.title())
                .author(productPostVm.author())
                .price(productPostVm.price())
                .category(categoryService.getCategoryById(productPostVm.categoryId()).orElse(null))
                .build());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
        productService.deleteBookById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<ProductGetVm>> searchProducts(String keyword) {
        return ResponseEntity.ok(productService.searchProduct(keyword)
                .stream()
                .map(ProductGetVm::from)
                .toList());
    }
    @GetMapping("/products/{productId}/details")
    @ResponseBody
    public Product getProductDetails(@PathVariable Long productId) {
        // Retrieve the product details based on the provided product ID
        Product product = productService.getBookByIdAll(productId);
        return product;
    }
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryGetVm>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories()
                .stream()
                .map(CategoryGetVm::from)
                .toList());
    }
    @RequestMapping(value = "/categories", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity addCategory(@RequestBody @Valid NewDTO newDTO) {
        Category category = new Category();
        category.setName(newDTO.getName());
        categoryService.addCategory(category);
        return new ResponseEntity(HttpStatus.CREATED);
    }
    @PutMapping("/categories/{id}")
    public ResponseEntity<Void> updateCategory(@PathVariable Long id, @RequestBody @NotNull CategoryPostVm categoryPostVm) {
        categoryService.updateCategory(Category.builder()
                .id(id)
                .name(categoryPostVm.name())
                .build());
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.ok().build();
    }

}
