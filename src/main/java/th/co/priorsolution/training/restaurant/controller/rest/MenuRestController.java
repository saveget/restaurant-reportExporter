package th.co.priorsolution.training.restaurant.controller.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.co.priorsolution.training.restaurant.entity.MenuItemEntity;
import th.co.priorsolution.training.restaurant.entity.OrderItemEntity;
import th.co.priorsolution.training.restaurant.model.DTO.MenuItemDTO;
import th.co.priorsolution.training.restaurant.model.DTO.OrderItemRequestDTO;
import th.co.priorsolution.training.restaurant.service.MenuService;

import java.awt.*;
import java.util.List;
@RestController
@RequestMapping("/api/menu")
@CrossOrigin
public class MenuRestController {
    private final MenuService menuService;

    public MenuRestController(MenuService menuService) {
        this.menuService = menuService;
    }
    @GetMapping("/available")
    public List<MenuItemDTO> getAvailableMenu() {
        return menuService.getAvailableMenuItems();
    }


    @PostMapping("/api/order")
    public ResponseEntity<String> submitOrder(@RequestBody List<OrderItemRequestDTO> items) {
        return ResponseEntity.ok("รับคำสั่งซื้อแล้ว");
    }
}