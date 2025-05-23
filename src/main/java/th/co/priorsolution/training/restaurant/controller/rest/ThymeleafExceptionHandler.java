package th.co.priorsolution.training.restaurant.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import th.co.priorsolution.training.restaurant.service.RoleRedirectService;

import java.nio.file.AccessDeniedException;
import java.time.Instant;

@ControllerAdvice
public class ThymeleafExceptionHandler {

    @Autowired
    private RoleRedirectService roleRedirectService;

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDenied(AccessDeniedException ex) {
        ModelAndView model = new ModelAndView("error/access-denied");
        model.addObject("errorTitle", "Access Denied");
        model.addObject("errorMessage", "คุณไม่มีสิทธิ์ในการเข้าถึงหน้านี้ กรุณากลับไปยังหน้าหลักของคุณ");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String redirectUrl = roleRedirectService.getRedirectUrl(authentication);

        model.addObject("homeUrl", redirectUrl);
        model.addObject("status", HttpStatus.FORBIDDEN.value());
        return model;
    }

}
