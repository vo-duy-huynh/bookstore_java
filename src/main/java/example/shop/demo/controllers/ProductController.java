package example.shop.demo.controllers;

import example.shop.demo.daos.Item;
import example.shop.demo.entities.Cover;
import example.shop.demo.entities.Product;
import example.shop.demo.repositories.IInvoiceRepository;
import example.shop.demo.repositories.IItemInvoiceRepository;
import example.shop.demo.services.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    private final CategoryService categoryService;

    private final CartService cartService;
    private final UserService userService;
    private final IInvoiceRepository invoiceRepository;
    private final IItemInvoiceRepository itemInvoiceRepository;
    private final FileUpload fileUpload;
    @GetMapping
    public String showAllProducts(@NotNull Model model,
                               @RequestParam(defaultValue = "0") Integer pageNo,
                               @RequestParam(defaultValue = "3") Integer pageSize,
                               @RequestParam(defaultValue = "id") String sortBy) {
        model.addAttribute("products", productService.getAllProducts(pageNo, pageSize, sortBy));
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("covers", productService.getCoverList());
        model.addAttribute("totalPages", productService.getAllProducts(pageNo, pageSize, sortBy).size() / pageSize);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product/list";
    }

    @GetMapping("/add")
    public String addProductForm(@NotNull Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product/add";
    }

    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute("product") Product product,
                           @RequestParam("imgList") MultipartFile[] images,
                           @NotNull BindingResult bindingResult,
                           @NotNull Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "product/edit";
        }
        productService.addProduct(product);
        for (MultipartFile image : images) {
            if (!image.isEmpty()) {
                try {
                    String imageUrl = fileUpload.uploadFile(image);
                    Cover productImage = new Cover();
                    if (product.getCovers().isEmpty()) {
                        productImage.setIsThumbnail(true);
                    }
                    else {
                        productImage.setIsThumbnail(false);
                    }
                    productImage.setUrlImage(imageUrl);
                    productImage.setProduct(product);
                    product.getCovers().add(productImage);
                    productService.addCover(productImage);
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle image upload error
                }
            }
        }
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String editBookForm(@NotNull Model model, @PathVariable long id) {
        var product = productService.getBookById(id);
        model.addAttribute("product", product.orElseThrow(() -> new IllegalArgumentException("Product not found")));
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("covers", productService.getCoverByBookId(id));
        return "product/edit";
    }

    @PostMapping("/edit")
    public String editProduct(@Valid @ModelAttribute("product") Product product,
                           @RequestParam("imgList") MultipartFile[] images,
                           @NotNull BindingResult bindingResult,
                           @NotNull Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "product/edit";
        }
        productService.updateProduct(product);
        for (MultipartFile image : images) {
            if (!image.isEmpty()) {
                try {
                    String imageUrl = fileUpload.uploadFile(image);
                    Cover productImage = new Cover();
                    if (product.getCovers().isEmpty()) {
                        productImage.setIsThumbnail(true);
                    }
                    else {
                        productImage.setIsThumbnail(false);
                    }
                    productImage.setUrlImage(imageUrl);
                    productImage.setProduct(product);
                    product.getCovers().add(productImage);
                    productService.addCover(productImage);
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle image upload error
                }
            }
        }
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable long id) {
        productService.getBookById(id)
                .ifPresentOrElse(
                        book -> productService.deleteBookById(id),
                        () -> { throw new IllegalArgumentException("Product not found"); }
                );
        return "redirect:/products";
    }
    @GetMapping("/set-view-image/{id}")
    public String setViewImage(@PathVariable int id, Model model) {
        //set view image = false all
        List<Cover> images = productService.getCoverByBookId(productService.getCoverById(id).getProduct().getId());
        for (Cover image : images) {
            image.setIsThumbnail(false);
            productService.addCover(image);
        }
        Cover image = productService.getCoverById(id);
        image.setIsThumbnail(true);
        productService.addCover(image);
        return "redirect:/products/edit/"+image.getProduct().getId();
    }
    @GetMapping("/delete-image/{id}")
    public String deleteImage(@PathVariable int id, Model model) {
        Cover image = productService.getCoverById(id);
        productService.removeCover(id);
        return "redirect:/products/edit/"+image.getProduct().getId();
    }
    @GetMapping("/search")
    public String searchProduct(
            @NotNull Model model,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "3") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        model.addAttribute("products", productService.searchProduct(keyword));
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("covers", productService.getCoverList());
        model.addAttribute("totalPages",
                productService
                        .getAllProducts(pageNo, pageSize, sortBy).size() / pageSize);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product/list";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(HttpSession session,
                            @RequestParam long id,
                            @RequestParam String name,
                            @RequestParam double price,
                            @RequestParam String cover,
                            @RequestParam(defaultValue = "1") int quantity) {

        var cart = cartService.getCart(session);
        cart.addItems(new Item(id, cover, name, price, quantity));
        cartService.updateCart(session, cart);
        return "redirect:/cart";
    }
    @GetMapping("/invoices")
    public String order(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("user", username);
        model.addAttribute("itemInvoices", itemInvoiceRepository.getItemInvoicesByUser(username));
        return "product/invoice";
    }
}
