package example.shop.demo.controllers;

import example.shop.demo.entities.User;
import example.shop.demo.services.UserService;
import example.shop.demo.viewmodels.UserGetVm;
import example.shop.demo.viewmodels.UserPostVm;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2")
@CrossOrigin("*")
@RequiredArgsConstructor
public class UserApiController {
    private final UserService userService;
    @GetMapping("/users")
    public ResponseEntity<List<UserGetVm>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll()
                .stream()
                .map(UserGetVm::from)
                .toList());
    }
    @PutMapping("/users/put/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @RequestBody @NotNull UserPostVm userPostVm) {
        userService.saveApi(User.builder()
                .id(id)
                .username(userPostVm.userName())
                .email(userPostVm.email())
                .phone(userPostVm.phone())
                .build());
        return ResponseEntity.ok().build();
    }
    @GetMapping("/profile-user")
    public ResponseEntity<UserGetVm> getUserById(Authentication authentication) {
        var username = authentication.getName();
        return ResponseEntity.ok(userService.findByUsername(username)
                .map(UserGetVm::from)
                .orElse(null));
    }
    @GetMapping("/users/{username}")
    public ResponseEntity<UserGetVm> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.findByUsername(username)
                .map(UserGetVm::from)
                .orElse(null));
    }
    @PutMapping("/users/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @RequestBody @NotNull User user) {
        user.setId(id);
        var password = userService.loadUserByUsername(user.getUsername()).getPassword();
        userService.saveApi(User.builder()
                .id(id)
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .provider(user.getProvider())
                .password(password)
                .build());
        return ResponseEntity.ok().build();
    }
    @PutMapping("/users/password/{id}")
    public ResponseEntity<Void> updateUserPassword(@PathVariable Long id, @RequestBody @NotNull User user) {
        user.setId(id);
        String hashedPassword = userService.hashPassword(user.getPassword());
        userService.savePasswordApi(User.builder()
                .id(id)
                .password(hashedPassword)
                .build());
        return ResponseEntity.ok().build();
    }
    @GetMapping("/users/{password}/{id}")
    public ResponseEntity<Boolean> checkPassword(@PathVariable String password, @PathVariable Long id) {
        boolean check = userService.checkPassword(password, id);
        return ResponseEntity.ok(userService.checkPassword(password, id));
    }
}
