package th.co.priorsolution.training.restaurant.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        if (role.equals("ROLE_WAITRESS")) {
            response.sendRedirect("/app/waitress");
        } else if (role.equals("ROLE_CHEF")) {
            response.sendRedirect("/app/kitchen");
        } else if (role.equals("ROLE_CUSTOMER")) {
            response.sendRedirect("/app/customer");
        } else {
            response.sendRedirect("/access-denied");
        }
    }
}
