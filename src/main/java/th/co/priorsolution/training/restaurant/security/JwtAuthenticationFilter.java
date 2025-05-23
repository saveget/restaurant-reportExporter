package th.co.priorsolution.training.restaurant.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;  // ตัวช่วยเช็คและอ่านข้อมูลจาก JWT

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String path = request.getServletPath();

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                if (jwtUtil.validateToken(token) &&
                        SecurityContextHolder.getContext().getAuthentication() == null) {

                    String username = jwtUtil.getUsernameFromToken(token);
                    String role = jwtUtil.getRoleFromToken(token);

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    username,
                                    null,
                                    List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                            );

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (ExpiredJwtException e) {
                handleUnauthorized(response, path);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void handleUnauthorized(HttpServletResponse response, String path) throws IOException {
        if (path.startsWith("/api/")) {
            // API ส่ง JSON 401
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Unauthorized\",\"message\":\"Authentication token was either missing or invalid.\"}");
        } else {
            // หน้าเว็บ redirect ไป /login
            response.sendRedirect("/login");
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        // path เหล่านี้ไม่ต้องเช็ค JWT — ปล่อยให้ Spring Security จัดการ session login
        return path.startsWith("/login") ||
                path.startsWith("/logout") ||
                path.startsWith("/css/") ||
                path.startsWith("/test/") ||
                path.startsWith("/error/") ||
                path.startsWith("/js/");
    }

}
