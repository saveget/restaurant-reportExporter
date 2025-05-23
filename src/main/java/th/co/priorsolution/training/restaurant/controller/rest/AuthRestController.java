package th.co.priorsolution.training.restaurant.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import th.co.priorsolution.training.restaurant.model.DTO.RefreshTokenRequest;
import th.co.priorsolution.training.restaurant.model.DTO.RefreshTokenResponse;
import th.co.priorsolution.training.restaurant.model.DTO.UserDTO;
import th.co.priorsolution.training.restaurant.model.LoginRequest;
import th.co.priorsolution.training.restaurant.security.JwtUtil;
import th.co.priorsolution.training.restaurant.service.AuthenticationService;
import th.co.priorsolution.training.restaurant.service.TokenService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    private final AuthenticationService authenticationService;
    private final TokenService tokenService;
    private final JwtUtil jwtUtil;

    public AuthRestController(AuthenticationService authenticationService, TokenService tokenService, JwtUtil jwtUtil) {
        this.authenticationService = authenticationService;
        this.tokenService = tokenService;
        this.jwtUtil = jwtUtil;
    }

    // Login API: รับ username/password, authenticate, สร้าง access + refresh token พร้อมเก็บ refresh token
    @PostMapping("/login")
    public ResponseEntity<RefreshTokenResponse> login(@RequestBody UserDTO.AuthRequest authRequest) {
        // authenticate user (เรียก service จริงจัง)
        authenticationService.authenticate(authRequest.getUsername(), authRequest.getPassword());

        // สร้าง token
        String accessToken = jwtUtil.generateToken(authRequest.getUsername(), "USER_ROLE"); // กำหนด role จริง ๆ จาก DB
        String refreshToken = jwtUtil.generateRefreshToken(authRequest.getUsername());

        // เก็บ refresh token
        tokenService.storeRefreshToken(authRequest.getUsername(), refreshToken);

        return ResponseEntity.ok(new RefreshTokenResponse(accessToken, refreshToken));
    }

    // Refresh token API: รับ refresh token, ตรวจสอบ, สร้าง access + refresh token ใหม่
    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        RefreshTokenResponse response = tokenService.refreshAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }
}
