package th.co.priorsolution.training.restaurant.controller.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import th.co.priorsolution.training.restaurant.service.RoleRedirectService;

@Controller
@RequestMapping("/error")
public class ErrorPageController {

    @Autowired
    private RoleRedirectService roleRedirectService;

    @GetMapping("/access-denied")
    public String accessDeniedPage(Model model, Authentication authentication) {
        model.addAttribute("errorTitle", "Access Denied");
        model.addAttribute("errorMessage", "คุณไม่ได้รับสิทธิ์ให้เข้าถึงข้อมูลนี้ กรุณากลับหน้าหลักของคุณ");

        String redirectUrl = roleRedirectService.getRedirectUrl(authentication);

        model.addAttribute("homeUrl", redirectUrl);

        return "error/access-denied";
    }
}
