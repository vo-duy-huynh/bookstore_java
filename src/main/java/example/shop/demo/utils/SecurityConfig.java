package example.shop.demo.utils;

import example.shop.demo.services.OAuthService;
import example.shop.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig{

    private final OAuthService oAuthService;

    private final UserService userService;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        var auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService());
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**","/assets/", "/", "/oauth/**", "/register", "/error", "/products/set-view-image/**", "/products/delete-image/**", "/assets/css/vendor", "/products/add/**", "/products/search/**", "/products/add-to-cart", "/login", "/profile-user")
                        .permitAll()
                        .requestMatchers("/products/edit/**", "/products/add/**", "/products/delete")
                        .hasAnyAuthority("ADMIN")
                        .requestMatchers("/products", "/cart", "/cart/**", "/categories/**", "/products/**", "/profile-user")
                        .hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/api/**, /api/categories/**, /api/v2/**")
                        .permitAll()
                        .anyRequest().authenticated()
                ).logout(logout ->
                        logout.logoutUrl("/logout")
                                .logoutSuccessUrl("/login")
                                .deleteCookies("JSESSIONID")
                                .invalidateHttpSession(true)
                                .clearAuthentication(true)
                                .permitAll()
                ).formLogin(formLogin ->
                        formLogin.loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/")
                        .failureUrl("/login?error=true")
                        .permitAll()
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(
                            (request, response, authentication) -> {
                                String role = authentication.getAuthorities().toString();
                                if (role.contains("ADMIN")) {
                                    response.sendRedirect("/admin");
                                } else {
                                    response.sendRedirect("/");
                                }

                            }
                        )

                ).oauth2Login(
                        oauth2Login -> oauth2Login
                                .loginPage("/login")
                                .failureUrl("/login?error")
                                .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                                        .userService(oAuthService)
                                )
                                .successHandler(
                                        (request, response, authentication) -> {
                                            var oidcUser = (DefaultOidcUser) authentication.getPrincipal();
                                            userService.saveOauthUser(oidcUser.getEmail(), oidcUser.getName());
                                            response.sendRedirect("/");
                                        }
                                )
                                .permitAll()
                )
                .rememberMe(rememberMe -> rememberMe
                        .key("hutech")
                        .rememberMeCookieName("hutech")
                        .tokenValiditySeconds(24 * 60 * 60)
                        .userDetailsService(userDetailsService())
                ).exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedPage("/403")
                ).sessionManagement(sessionManagement -> sessionManagement
                        .maximumSessions(1)
                        .expiredUrl("/login")
                ).httpBasic(httpBasic -> httpBasic
                        .realmName("hutech")
                ).build();
    }

}