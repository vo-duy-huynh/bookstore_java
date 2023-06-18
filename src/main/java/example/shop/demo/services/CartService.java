package example.shop.demo.services;

import example.shop.demo.daos.Cart;
import example.shop.demo.daos.Item;
import example.shop.demo.entities.Invoice;
import example.shop.demo.entities.ItemInvoice;
import example.shop.demo.entities.User;
import example.shop.demo.repositories.IInvoiceRepository;
import example.shop.demo.repositories.IItemInvoiceRepository;
import example.shop.demo.repositories.IProductRepository;
import example.shop.demo.repositories.IUserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private static final String CART_SESSION_KEY = "cart";

    private final IInvoiceRepository invoiceRepository;

    private final IItemInvoiceRepository itemInvoiceRepository;

    private final IProductRepository bookRepository;
    private  final IUserRepository userRepository;

    @PreAuthorize("hasAnyAuthority('USER') or hasAnyAuthority('ADMIN')")
    public Cart getCart(@NotNull HttpSession session) {
        return Optional.ofNullable((Cart) session.getAttribute(CART_SESSION_KEY))
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    session.setAttribute(CART_SESSION_KEY, cart);
                    return cart;
                });
    }

    @PreAuthorize("hasAnyAuthority('USER') or hasAnyAuthority('ADMIN')")
    public void updateCart(@NotNull HttpSession session, Cart cart) {
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public void removeCart(@NotNull HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }

    @PreAuthorize("hasAnyAuthority('USER') or hasAnyAuthority('ADMIN')")
    public int getSumQuantity(@NotNull HttpSession session) {
        return getCart(session).getCartItems().stream()
                .mapToInt(Item::getQuantity)
                .sum();
    }
    public int getSumItem(@NotNull HttpSession session) {
        return getCart(session).getCartItems().size();
    }
    @PreAuthorize("hasAnyAuthority('USER') or hasAnyAuthority('ADMIN')")
    public double getSumPrice(@NotNull HttpSession session) {
        return getCart(session).getCartItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    @PreAuthorize("hasAnyAuthority('USER') or hasAnyAuthority('ADMIN')")
    public void saveCart(@NotNull HttpSession session, String username) {
        var cart = getCart(session);
        if (cart.getCartItems().isEmpty()) return;

        var invoice = new Invoice();
        invoice.setInvoiceDate(new Date(new Date().getTime()));
        invoice.setPrice(getSumPrice(session));
        User user = userRepository.findUserByUsername(username);
        invoice.setUser(user);
        invoiceRepository.save(invoice);

        cart.getCartItems().forEach(item -> {
            var items = new ItemInvoice();
            items.setInvoice(invoice);
            items.setCover(item.getCover());
            items.setQuantity(item.getQuantity());
            items.setProduct(bookRepository.findById(item.getProductId()).orElseThrow());
            itemInvoiceRepository.save(items);
        });

        removeCart(session);
    }
}
