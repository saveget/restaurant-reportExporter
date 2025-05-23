package th.co.priorsolution.training.restaurant.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import th.co.priorsolution.training.restaurant.entity.UserEntity;
import th.co.priorsolution.training.restaurant.model.DTO.RefreshTokenResponse;
import th.co.priorsolution.training.restaurant.model.LoginRequest;
import th.co.priorsolution.training.restaurant.repository.UserRepository;
import th.co.priorsolution.training.restaurant.security.JwtUtil;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final TokenService tokenService; // เพิ่มเข้ามาเพื่อเก็บ refresh token

    public AuthenticationService(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 JwtUtil jwtUtil,
                                 TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.tokenService = tokenService;
    }

    // Authenticate user credentials, throws exception if invalid
    public UserEntity authenticateUser(String username, String password) {
        UserEntity user = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");  // หรือ BadCredentialsException ก็ได้
        }
        return user;
    }

    // รวม login: authenticate + generate access + refresh token + store refresh token
    public RefreshTokenResponse login(String username, String password) {
        UserEntity user = authenticateUser(username, password);

        String accessToken = jwtUtil.generateToken(user.getName(), user.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(user.getName());

        // เก็บ refresh token
        tokenService.storeRefreshToken(user.getName(), refreshToken);

        return new RefreshTokenResponse(accessToken, refreshToken);
    }

    // ถ้าจะรับ LoginRequest เป็น parameter
    public RefreshTokenResponse login(LoginRequest loginRequest) {
        return login(loginRequest.getUsername(), loginRequest.getPassword());
    }
}
