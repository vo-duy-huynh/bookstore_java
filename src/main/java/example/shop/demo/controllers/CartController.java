package example.shop.demo.controllers;

import example.shop.demo.dto.PaymentResDTO;
import example.shop.demo.dto.TransactionDTO;
import example.shop.demo.services.CartService;
import example.shop.demo.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final UserService userService;
    @GetMapping
    public String showCart(HttpSession session,
                           @NotNull Model model) {
        model.addAttribute("cart", cartService.getCart(session));
        model.addAttribute("totalPrice", cartService.getSumPrice(session));
        model.addAttribute("totalQuantity", cartService.getSumQuantity(session));
        return "product/cart";
    }

    @GetMapping("/removeFromCart/{id}")
    public String removeFromCart(HttpSession session,
                                 @PathVariable Long id) {
        var cart = cartService.getCart(session);
        cart.removeItems(id);
        return "redirect:/cart";
    }

    @GetMapping("/updateCart/{id}/{quantity}")
    public String updateCart(HttpSession session,
                             @PathVariable Long id,
                             @PathVariable int quantity) {
        var cart = cartService.getCart(session);
        cart.updateItems(id, quantity);
        return "redirect:/cart";
    }

    @GetMapping("/clearCart")
    public String clearCart(HttpSession session) {
        cartService.removeCart(session);
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkout(HttpSession session) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        cartService.saveCart(session, username);

        return "redirect:/cart";
    }
    @GetMapping("/payment-confirm")
    public String paymentConfirm(
            @RequestParam("vnp_ResponseCode") String vnp_ResponseCode,
            @RequestParam("vnp_Amount") String vnp_Amount,
            @RequestParam("vnp_OrderInfo") String vnp_OrderInfo,
            @RequestParam("vnp_BankCode") String vnp_BankCode,
            @RequestParam("vnp_PayDate") String vnp_PayDate, Model model, HttpSession session
    ) {
        var user = userService.getUserByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        TransactionDTO transactionDTO = new TransactionDTO();
        if (vnp_ResponseCode.equals("00")) {

            transactionDTO.setAmount(Long.parseLong(vnp_Amount)/100);
            transactionDTO.setBankCode(vnp_BankCode);
            transactionDTO.setOrderDescription(vnp_OrderInfo);
            transactionDTO.setCreateDate(vnp_PayDate);
            transactionDTO.setOrderType("Checkout by VNPAY");
            transactionDTO.setStatus("Success");
            transactionDTO.setMessage("Thank you for your purchase!");
            cartService.removeCart(session);
        } else {
            transactionDTO.setAmount(Long.parseLong(vnp_Amount)/100);
            transactionDTO.setBankCode(vnp_BankCode);
            transactionDTO.setOrderDescription(vnp_OrderInfo);
            transactionDTO.setCreateDate(vnp_PayDate);
            transactionDTO.setOrderType("Checkout by VNPAY");
            transactionDTO.setStatus("Failed");
            transactionDTO.setMessage("Your payment is failed!");
        }
        model.addAttribute("transaction", transactionDTO);
        model.addAttribute("customer", user);
        return "product/payment-confirm";
    }

}
