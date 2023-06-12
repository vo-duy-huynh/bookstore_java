package fit.hutech.spring.controllers;

import fit.hutech.spring.dto.UserDTO;
import fit.hutech.spring.entities.Role;
import fit.hutech.spring.entities.User;
import fit.hutech.spring.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    @GetMapping("/details/{username}")
    public String profile(@NotNull @PathVariable String username, Model model){
        Optional<User> opUser = userService.findByUsername(username);
        if(opUser.isPresent()){
            User user = opUser.get();
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setUsername(user.getUsername());
            userDTO.setPhone(user.getPhone());
            userDTO.setEmail(user.getEmail());
            userDTO.setProvider(user.getProvider());
            userDTO.setPassword(user.getPassword());
            List<Long> roleIds = new ArrayList<>();
            for (Role item:user.getRoles()) {
                roleIds.add(item.getId());
            }
            userDTO.setRoleIds(roleIds);
            model.addAttribute("userDTO", userDTO);
            List<Role> roles = new ArrayList<>();
            Role role = new Role();
            role.setId(1L);
            role.setName("ADMIN");
            roles.add(role);
            Role role2 = new Role();
            role2.setId(2L);
            role2.setName("USER");
            roles.add(role2);
            model.addAttribute("roles", roles);
        }
        return "user/profile";
    }
}