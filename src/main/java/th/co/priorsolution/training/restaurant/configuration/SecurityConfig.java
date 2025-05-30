package th.co.priorsolution.training.restaurant.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import th.co.priorsolution.training.restaurant.security.JwtAuthenticationEntryPoint;
import th.co.priorsolution.training.restaurant.security.JwtAuthenticationFilter;
import th.co.priorsolution.training.restaurant.service.CustomUserDetailsService;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/login",
                                "/login",
                                "/css/**",
                                "/js/**",
                                "/app/",
                                "/app/index"
                        ).permitAll()

                        .requestMatchers("/app/waitress/**").hasRole("WAITRESS")
                        .requestMatchers("/app/kitchen/grill/**").hasRole("CHEF_GRILL")
                        .requestMatchers("/app/kitchen/salad/**").hasRole("CHEF_SALAD")
                        .requestMatchers("/app/kitchen/beverage/**").hasRole("CHEF_BEVERAGE")
                        .requestMatchers("/app/kitchen/pasta/**").hasRole("CHEF_PASTA")
                        .requestMatchers("/app/manager/**").hasRole("MANAGER")

                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler((request, response, authentication) -> {
                            String role = authentication.getAuthorities().stream()
                                    .findFirst()
                                    .map(auth -> auth.getAuthority())
                                    .orElse("");

                            switch (role) {
                                case "ROLE_WAITRESS" -> response.sendRedirect("/app/waitress");
                                case "ROLE_MANAGER" -> response.sendRedirect("/app/manager");
                                case "ROLE_CHEF_GRILL" -> response.sendRedirect("/app/kitchen/grill");
                                case "ROLE_CHEF_SALAD" -> response.sendRedirect("/app/kitchen/salad");
                                case "ROLE_CHEF_BEVERAGE" -> response.sendRedirect("/app/kitchen/beverage");
                                case "ROLE_CHEF_PASTA" -> response.sendRedirect("/app/kitchen/pasta");
                                case "ROLE_CUSTOMER" -> response.sendRedirect("/app/");
                                default -> response.sendRedirect("/app/");
                            }
                        })
                        .failureUrl("/login?error=true")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                )

                .exceptionHandling(ex -> ex
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            // Redirect ไปหน้า error แบบ HTML
                            response.sendRedirect("/error/access-denied");
                        })
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // เปลี่ยนจาก NoOp
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }
}
