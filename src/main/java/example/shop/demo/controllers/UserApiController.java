package example.shop.demo.controllers;

import example.shop.demo.entities.User;
import example.shop.demo.services.UserService;
import example.shop.demo.viewmodels.UserGetVm;
import example.shop.demo.viewmodels.UserPostVm;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    @GetMapping("/users/{id}")
    public ResponseEntity<UserGetVm> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id)
                .map(UserGetVm::from)
                .orElse(null));
    }
}
