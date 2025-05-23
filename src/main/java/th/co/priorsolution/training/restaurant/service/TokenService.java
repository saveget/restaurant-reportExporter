package th.co.priorsolution.training.restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import th.co.priorsolution.training.restaurant.exception.CustomException;
import th.co.priorsolution.training.restaurant.model.DTO.RefreshTokenResponse;
import th.co.priorsolution.training.restaurant.security.JwtUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenService {

    // สมมติเก็บ refresh token แบบง่าย ๆ ใน Map
    private final Map<String, String> refreshTokenStorage = new ConcurrentHashMap<>();

    @Autowired
    private JwtUtil jwtUtil;

    public RefreshTokenResponse refreshAccessToken(String refreshToken) {
        if (jwtUtil.validateRefreshToken(refreshToken)) {
            String username = jwtUtil.getUsernameFromRefreshToken(refreshToken);

            // ตรวจสอบว่า refresh token นี้อยู่ใน storage หรือไม่ (ถ้ามีระบบ revoke)
            String storedToken = refreshTokenStorage.get(username);
            if (storedToken == null || !storedToken.equals(refreshToken)) {
                throw new CustomException("REFRESH_TOKEN_INVALID", "Refresh token ไม่ถูกต้อง", HttpStatus.UNAUTHORIZED);
            }

            // สร้าง access token ใหม่
            String newAccessToken = jwtUtil.generateToken(username, "USER_ROLE"); // กำหนด role ให้เหมาะสม
            String newRefreshToken = jwtUtil.generateRefreshToken(username);

            // อัพเดต refresh token ใน storage
            refreshTokenStorage.put(username, newRefreshToken);

            return new RefreshTokenResponse(newAccessToken, newRefreshToken);
        } else {
            throw new CustomException("REFRESH_TOKEN_EXPIRED", "Refresh token หมดอายุหรือไม่ถูกต้อง", HttpStatus.UNAUTHORIZED);
        }
    }

    public void storeRefreshToken(String username, String refreshToken) {
        refreshTokenStorage.put(username, refreshToken);
    }

    public void revokeRefreshToken(String username) {
        refreshTokenStorage.remove(username);
    }
}
