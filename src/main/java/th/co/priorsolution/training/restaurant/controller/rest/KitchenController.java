package th.co.priorsolution.training.restaurant.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.co.priorsolution.training.restaurant.entity.OrderItemEntity;
import th.co.priorsolution.training.restaurant.repository.OrderItemRepository;
import th.co.priorsolution.training.restaurant.repository.OrderRepository;
import th.co.priorsolution.training.restaurant.service.KitchenService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/kitchen")
@CrossOrigin(origins = "*")
public class KitchenController {

    @Autowired
    private KitchenService kitchenService;

    @GetMapping("/orders")
    public List<OrderItemEntity> getOrdersByStation(@RequestParam String station) {
        return kitchenService.getOrdersByStation(station);
    }

    @PutMapping("/orders/{id}/status")
    public Map<String, Object> updateOrderItemStatus(@PathVariable Integer id,
                                                     @RequestBody Map<String, String> body) {
        return kitchenService.updateOrderItemStatus(id, body.get("status"));
    }


}
