package example.shop.demo.controllers;

import example.shop.demo.repositories.IInvoiceRepository;
import example.shop.demo.repositories.IItemInvoiceRepository;
import example.shop.demo.services.ProductService;
import example.shop.demo.services.CartService;
import example.shop.demo.services.CategoryService;
import example.shop.demo.services.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {
    private final ProductService productService;

    private final CategoryService categoryService;

    private final CartService cartService;
    private final UserService userService;
    private final IInvoiceRepository invoiceRepository;
    private final IItemInvoiceRepository itemInvoiceRepository;
    @GetMapping("/")
    public String showAllProducts(@NotNull Model model) {
        model.addAttribute("products", productService.findAllProducts());
        model.addAttribute("covers", productService.getCoverList());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "home/index";
    }
    @GetMapping("/shop")
    public String showAllProductShops(@NotNull Model model,
                               @RequestParam(defaultValue = "0") Integer pageNo,
                               @RequestParam(defaultValue = "8") Integer pageSize,
                               @RequestParam(defaultValue = "id") String sortBy) {
        model.addAttribute("products", productService.getAllProducts(pageNo, pageSize, sortBy));
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("covers", productService.getCoverList());
        model.addAttribute("totalPages", productService.getAllProducts(pageNo, pageSize, sortBy).size() / pageSize);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "home/shop";
    }
    @GetMapping("/error/403")
    public String error403() {
        return "error/403";
    }
    @GetMapping("/error/404")
    public String error404() {
        return "error/404";
    }
    @GetMapping("/error/500")
    public String error500() {
        return "error/500";
    }
}