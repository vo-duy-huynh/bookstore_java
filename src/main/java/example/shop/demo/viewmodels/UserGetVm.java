package example.shop.demo.viewmodels;

import example.shop.demo.entities.Role;
import example.shop.demo.entities.User;
import lombok.Builder;

@Builder
public record UserGetVm(Long id, String userName, String email, String password, String phone, String roleName) {
    public static UserGetVm from(User user) {
        String roleName = user.getRoles().stream()
                .findFirst()
                .map(Role::getName)
                .orElse(null);

        return new UserGetVm(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getPhone(),
                roleName
        );
    }
}
