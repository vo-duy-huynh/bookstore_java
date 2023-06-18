package example.shop.demo.services;

import example.shop.demo.constants.Provider;
import example.shop.demo.constants.Role;
import example.shop.demo.entities.User;
import example.shop.demo.repositories.IRoleRepository;
import example.shop.demo.repositories.IUserRepository;
import example.shop.demo.viewmodels.UserPostVm;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    public void save(@NotNull User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }
    public void saveApi(@NotNull User user) {
        User user1 = userRepository.findById(user.getId()).orElse(null);
        user1.setProvider(user.getProvider());
        user1.setEmail(user.getEmail());
        user1.setPhone(user.getPhone());
        user1.setUsername(user.getUsername());
        userRepository.save(user1);
    }
    public void savePasswordApi(@NotNull User user) {
        User user1 = userRepository.findById(user.getId()).orElse(null);
        user1.setPassword(user.getPassword());
        userRepository.save(user1);
    }
    public void setDefaultRole(String username) {
        userRepository.findByUsername(username).ifPresentOrElse(
                u -> {
                    u.getRoles().add(roleRepository.findRoleById(Role.USER.value));
                    userRepository.save(u);
                },
                () -> {
                    throw new UsernameNotFoundException("User not found");
                }
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .accountExpired(!user.isAccountNonExpired())
                .accountLocked(!user.isAccountNonLocked())
                .credentialsExpired(!user.isCredentialsNonExpired())
                .disabled(!user.isEnabled())
                .build();
    }

    public Optional<User> findByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public void saveOauthUser(String email, @NotNull String username, String clientName) {
        if (userRepository.findByUsername(username).isPresent())
            return;

        var user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(new BCryptPasswordEncoder().encode(username));
        if (clientName.toUpperCase().equals(Provider.GOOGLE.value.toUpperCase()))
            user.setProvider(Provider.GOOGLE.value);
        else
        {
            user.setProvider(Provider.GITHUB.value);
        }
        user.getRoles().add(roleRepository.findRoleById(Role.USER.value));
        userRepository.save(user);
    }

    public void getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        System.out.println(currentPrincipalName);
    }
    public List<User> findAll(){
        return userRepository.findAll();
    }

    public void updateUser(Long userId, UserPostVm userPostVm) {
        User user = userRepository.findByIdUser(userId);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        user.setUsername(userPostVm.getUserName());
        user.setEmail(userPostVm.getEmail());
//        user.setPassword(new BCryptPasswordEncoder().encode(userPostVm.getPassword()));
        user.setPhone(userPostVm.getPhone());
        userRepository.save(user);
    }

    public Optional<User> findById(Long userId) throws UsernameNotFoundException {
        return userRepository.findById(userId);
    }

    @PreAuthorize("hasAnyAuthority('USER') or hasAnyAuthority('ADMIN')")
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public String hashPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    public Boolean checkPassword(String password, Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new BCryptPasswordEncoder().matches(password, user.getPassword());
    }
    public User getUserByUserName(String username) {
        return userRepository.findUserByUsername(username);
    }
}
