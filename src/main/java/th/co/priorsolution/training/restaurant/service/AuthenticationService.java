package th.co.priorsolution.training.restaurant.service;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import th.co.priorsolution.training.restaurant.entity.UserEntity;
import th.co.priorsolution.training.restaurant.model.LoginRequest;
import th.co.priorsolution.training.restaurant.repository.UserRepository;
import th.co.priorsolution.training.restaurant.security.JwtUtil;

import java.io.IOException;
import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public String login(String username, String password) {
        UserEntity user = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // สร้าง JWT token โดยส่ง username กับ role เป็นพารามิเตอร์
        return jwtUtil.generateToken(user.getName(), user.getRole());
    }

    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            String redirectUrl = "/app/index";

            for (GrantedAuthority authority : authentication.getAuthorities()) {
                String role = authority.getAuthority();

                if (role.equals("ROLE_WAITRESS")) {
                    redirectUrl = "/app/waitress";
                    break;
                } else if (role.equals("ROLE_CHEF")) {
                    redirectUrl = "/app/kitchen";
                    break;
                } else if (role.equals("ROLE_CUSTOMER")) {
                    redirectUrl = "/app/customer";
                    break;
                }
            }

            response.sendRedirect(redirectUrl);
        };
    }


    public String authenticate(LoginRequest loginRequest) {
        Optional<UserEntity> optionalUser = userRepository.findByName(loginRequest.getUsername());

        if (optionalUser.isEmpty() ||
                !passwordEncoder.matches(loginRequest.getPassword(), optionalUser.get().getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        UserEntity user = optionalUser.get();
        return jwtUtil.generateToken(user.getName(), user.getRole());
    }
}
