package th.co.priorsolution.training.restaurant.controller.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import th.co.priorsolution.training.restaurant.entity.OrderItemEntity;
import th.co.priorsolution.training.restaurant.service.OrderService;

import java.util.List;

@Controller
@RequestMapping("/app")
public class HomeController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/kitchen/{station}")
    public String kitchenByStation(@PathVariable String station, Model model) {
        model.addAttribute("station", station);
        return "kitchen";  // ส่งไป kitchen.html
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("key", "value");  // ใช้งานได้แน่นอนแล้
        return "index"; // ไปยัง templates/index.html
    }

    @GetMapping("/waitress")
    public String tableOrdersPage() {
        return "waitress";  // จะโหลดจาก templates
    }





}



