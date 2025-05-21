package th.co.priorsolution.training.restaurant.controller.rest;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import th.co.priorsolution.training.restaurant.entity.OrderEntity;
import th.co.priorsolution.training.restaurant.entity.OrderItemEntity;
import th.co.priorsolution.training.restaurant.repository.OrderItemRepository;
import th.co.priorsolution.training.restaurant.repository.OrderRepository;
import th.co.priorsolution.training.restaurant.service.RestaurantTableService;
import th.co.priorsolution.training.restaurant.service.WaitressService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/waitress")
@CrossOrigin(origins = "*")
public class WaitressRestController {

    @Autowired
    private WaitressService waitressService;

    @GetMapping("/orders/{tableId}")
    public List<Map<String, Object>> getOrdersByTable(@PathVariable Integer tableId) {
        return waitressService.getOrdersByTable(tableId);
    }

    @GetMapping("/bill/{tableId}")
    public String getBill(@PathVariable Integer tableId) {
        return waitressService.calculateBill(tableId);
    }

    @PostMapping("/reset/{tableId}")
    public Map<String, String> resetTable(@PathVariable Integer tableId) {
        return waitressService.resetTable(tableId);
    }
}
