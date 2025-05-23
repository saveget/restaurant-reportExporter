package th.co.priorsolution.training.restaurant.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import th.co.priorsolution.training.restaurant.entity.UserEntity;
import th.co.priorsolution.training.restaurant.model.LoginRequest;
import th.co.priorsolution.training.restaurant.repository.UserRepository;
import th.co.priorsolution.training.restaurant.security.JwtUtil;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // Authenticate user credentials, throws exception if invalid
    public void authenticate(String username, String password) {
        UserEntity user = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");  // หรือ BadCredentialsException ก็ได้
        }
    }

    // Generate access token (ถ้าต้องการแยกออกจาก authenticate)
    public String generateAccessToken(String username) {
        UserEntity user = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return jwtUtil.generateToken(user.getName(), user.getRole());
    }

    // Helper method รวบ login (authenticate + generate token) ถ้าจำเป็น
    public String login(String username, String password) {
        authenticate(username, password);
        return generateAccessToken(username);
    }

    // หรือใช้ LoginRequest แทนรับ parameters แยกก็ได้
    public String authenticate(LoginRequest loginRequest) {
        return login(loginRequest.getUsername(), loginRequest.getPassword());
    }
}
