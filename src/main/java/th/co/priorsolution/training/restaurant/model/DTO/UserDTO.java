package th.co.priorsolution.training.restaurant.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

public class UserDTO {

    @Data
    public class AuthRequest {
        private String username;
        private String password;
    }

    @Data
    @AllArgsConstructor
    public class AuthResponse {
        private String jwt;
    }

}
