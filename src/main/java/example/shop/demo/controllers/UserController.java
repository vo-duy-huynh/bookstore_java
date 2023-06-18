package example.shop.demo.controllers;

import example.shop.demo.entities.Role;
import example.shop.demo.entities.User;
import example.shop.demo.services.UserService;
import example.shop.demo.dto.UserDTO;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @GetMapping("/register")
    public String register(@NotNull Model model) {
        model.addAttribute("user", new User());
        return "user/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user,
                           @NotNull BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "user/register";
        }
        userService.save(user);
        userService.setDefaultRole(user.getUsername());
        return "redirect:/login";
    }
    @GetMapping("/profile-user")
    public String profileUser(Authentication authentication, Model model){
        var username = authentication.getName();
        var user = userService.findByUsername(username);
        model.addAttribute("user", user);
        return "user/profile";
    }
    @GetMapping("/admin/users")
    public String users(){
        return "user/list";
    }
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/login";
    }
}