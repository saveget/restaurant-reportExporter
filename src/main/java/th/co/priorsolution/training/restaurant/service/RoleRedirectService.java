package th.co.priorsolution.training.restaurant.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class RoleRedirectService {

    public String getRedirectUrl(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "/login"; // fallback กรณีไม่ล็อกอิน
        }

        var authorities = authentication.getAuthorities();

        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_WAITRESS"))) {
            return "/app/waitress";
        } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"))) {
            return "/app/manager";
        } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_CHEF_GRILL"))) {
            return "/app/kitchen/grill";
        } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_CHEF_PASTA"))) {
            return "/app/kitchen/pasta";
        }else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_CHEF_SALAD"))) {
            return "/app/kitchen/salad";
        }else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_CHEF_BEVERAGE"))) {
            return "/app/kitchen/beverage";
        }else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"))) {
            return "/app/";
        }

        // เพิ่ม role อื่น ๆ ตามต้องการที่นี่
        return "/app/"; // default
    }
}