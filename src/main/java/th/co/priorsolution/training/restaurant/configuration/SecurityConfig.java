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
import th.co.priorsolution.training.restaurant.service.CustomUserDetailsService;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/login",
                                "/login",
                                "/css/**",
                                "/js/**",
                                "/app/",           // อนุญาตหน้า index
                                "/app/index",      // อนุญาตหน้า index
                                "/app/customer"    // หากคุณมีหน้า customer แยก
                        ).permitAll()

                        .requestMatchers("/app/waitress/**").hasRole("WAITRESS")
                        .requestMatchers("/app/kitchen/grill/**").hasRole("CHEF_GRILL")
                        .requestMatchers("/app/kitchen/salad/**").hasRole("CHEF_SALAD")
                        .requestMatchers("/app/kitchen/beverage/**").hasRole("CHEF_BEVERAGE")
                        .requestMatchers("/app/kitchen/pasta/**").hasRole("CHEF_PASTA")

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

                            if ("ROLE_WAITRESS".equals(role)) {
                                response.sendRedirect("/app/waitress");
                            } else if ("ROLE_CHEF_GRILL".equals(role)) {
                                response.sendRedirect("/app/kitchen/grill");
                            } else if ("ROLE_CHEF_SALAD".equals(role)) {
                                response.sendRedirect("/app/kitchen/salad");
                            } else if ("ROLE_CHEF_BEVERAGE".equals(role)) {
                                response.sendRedirect("/app/kitchen/beverage");
                            } else if ("ROLE_CHEF_PASTA".equals(role)) {
                                response.sendRedirect("/app/kitchen/pasta");
                            } else if ("ROLE_CUSTOMER".equals(role)) {
                                response.sendRedirect("/app/");
                            } else {
                                response.sendRedirect("/app/");
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
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendRedirect("/login");
                        })
                );

        return http.build();
    }


    // เปลี่ยนตรงนี้
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }



}