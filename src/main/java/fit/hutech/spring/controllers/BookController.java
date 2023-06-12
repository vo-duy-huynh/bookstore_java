package fit.hutech.spring.controllers;

import fit.hutech.spring.entities.Book;
import fit.hutech.spring.daos.Item;
import fit.hutech.spring.entities.Cover;
import fit.hutech.spring.entities.Invoice;
import fit.hutech.spring.repositories.IInvoiceRepository;
import fit.hutech.spring.repositories.IItemInvoiceRepository;
import fit.hutech.spring.services.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    private final CategoryService categoryService;

    private final CartService cartService;
    private final UserService userService;
    private final IInvoiceRepository invoiceRepository;
    private final IItemInvoiceRepository itemInvoiceRepository;
    private final FileUpload fileUpload;
    @GetMapping
    public String showAllBooks(@NotNull Model model,
                               @RequestParam(defaultValue = "0") Integer pageNo,
                               @RequestParam(defaultValue = "3") Integer pageSize,
                               @RequestParam(defaultValue = "id") String sortBy) {
        model.addAttribute("books", bookService.getAllBooks(pageNo, pageSize, sortBy));
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("covers", bookService.getCoverList());
        model.addAttribute("totalPages", bookService.getAllBooks(pageNo, pageSize, sortBy).size() / pageSize);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "book/list";
    }

    @GetMapping("/add")
    public String addBookForm(@NotNull Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "book/add";
    }

    @PostMapping("/add")
    public String addBook(@Valid @ModelAttribute("book") Book book,
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
            return "book/edit";
        }
        bookService.addBook(book);
        for (MultipartFile image : images) {
            if (!image.isEmpty()) {
                try {
                    String imageUrl = fileUpload.uploadFile(image);
                    Cover bookImage = new Cover();
                    if (book.getCover().isEmpty()) {
                        bookImage.setIsThumbnail(true);
                    }
                    else {
                        bookImage.setIsThumbnail(false);
                    }
                    bookImage.setUrlImage(imageUrl);
                    bookImage.setBook(book);
                    book.getCover().add(bookImage);
                    bookService.addCover(bookImage);
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle image upload error
                }
            }
        }
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String editBookForm(@NotNull Model model, @PathVariable long id) {
        var book = bookService.getBookById(id);
        model.addAttribute("book", book.orElseThrow(() -> new IllegalArgumentException("Book not found")));
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("covers", bookService.getCoverByBookId(id));
        return "book/edit";
    }

    @PostMapping("/edit")
    public String editBook(@Valid @ModelAttribute("book") Book book,
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
            return "book/edit";
        }
        bookService.updateBook(book);
        for (MultipartFile image : images) {
            if (!image.isEmpty()) {
                try {
                    String imageUrl = fileUpload.uploadFile(image);
                    Cover bookImage = new Cover();
                    if (book.getCover().isEmpty()) {
                        bookImage.setIsThumbnail(true);
                    }
                    else {
                        bookImage.setIsThumbnail(false);
                    }
                    bookImage.setUrlImage(imageUrl);
                    bookImage.setBook(book);
                    book.getCover().add(bookImage);
                    bookService.addCover(bookImage);
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle image upload error
                }
            }
        }
        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable long id) {
        bookService.getBookById(id)
                .ifPresentOrElse(
                        book -> bookService.deleteBookById(id),
                        () -> { throw new IllegalArgumentException("Book not found"); }
                );
        return "redirect:/books";
    }
    @GetMapping("/set-view-image/{id}")
    public String setViewImage(@PathVariable int id, Model model) {
        //set view image = false all
        List<Cover> images = bookService.getCoverByBookId(bookService.getCoverById(id).getBook().getId());
        for (Cover image : images) {
            image.setIsThumbnail(false);
            bookService.addCover(image);
        }
        Cover image = bookService.getCoverById(id);
        image.setIsThumbnail(true);
        bookService.addCover(image);
        return "redirect:/books/edit/"+image.getBook().getId();
    }
    @GetMapping("/delete-image/{id}")
    public String deleteImage(@PathVariable int id, Model model) {
        Cover image = bookService.getCoverById(id);
        bookService.removeCover(id);
        return "redirect:/books/edit/"+image.getBook().getId();
    }
    @GetMapping("/search")
    public String searchBook(
            @NotNull Model model,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "3") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        model.addAttribute("books", bookService.searchBook(keyword));
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("covers", bookService.getCoverList());
        model.addAttribute("totalPages",
                bookService
                        .getAllBooks(pageNo, pageSize, sortBy).size() / pageSize);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "book/list";
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
        return "book/invoice";
    }
}
