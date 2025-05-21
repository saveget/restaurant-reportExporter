package th.co.priorsolution.training.restaurant.controller.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import th.co.priorsolution.training.restaurant.entity.UserEntity;
import th.co.priorsolution.training.restaurant.model.LoginRequest;
import th.co.priorsolution.training.restaurant.model.LoginResponse;
import th.co.priorsolution.training.restaurant.repository.UserRepository;
import th.co.priorsolution.training.restaurant.security.JwtUtil;
import th.co.priorsolution.training.restaurant.service.AuthenticationService;

import java.util.Optional;
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            String token = authService.authenticate(loginRequest);
            return ResponseEntity.ok(new LoginResponse(token));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        }
    }

}






