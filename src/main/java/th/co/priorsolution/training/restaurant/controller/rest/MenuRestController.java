package th.co.priorsolution.training.restaurant.controller.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.co.priorsolution.training.restaurant.entity.MenuItemEntity;
import th.co.priorsolution.training.restaurant.entity.OrderItemEntity;
import th.co.priorsolution.training.restaurant.service.MenuService;

import java.awt.*;
import java.util.List;
@RestController
@RequestMapping("/api/menu")
@CrossOrigin // เผื่อเชื่อม frontend
public class MenuRestController {
    private final MenuService menuService;

    public MenuRestController(MenuService menuService) {
        this.menuService = menuService;
    }
    @GetMapping("/available")
    public List<MenuItemEntity> getAvailableMenu() {
        return menuService.getAvailableMenuItems();
    }

    @PostMapping("/api/order")
    public ResponseEntity<String> submitOrder(@RequestBody List<OrderItemEntity> items) {
        // ทำการบันทึกคำสั่งซื้อที่ส่งมาจาก frontend
        return ResponseEntity.ok("รับคำสั่งซื้อแล้ว");
    }
}