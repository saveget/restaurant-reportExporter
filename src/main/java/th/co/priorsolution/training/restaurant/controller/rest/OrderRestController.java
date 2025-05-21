package th.co.priorsolution.training.restaurant.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import th.co.priorsolution.training.restaurant.entity.OrderEntity;
import th.co.priorsolution.training.restaurant.repository.OrderItemRepository;
import th.co.priorsolution.training.restaurant.repository.OrderRepository;
import th.co.priorsolution.training.restaurant.service.OrderService;
import th.co.priorsolution.training.restaurant.model.DTO.OrderRequestDTO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/order")
@CrossOrigin(origins = "*")
public class OrderRestController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public Map<String, Object> createOrder(@RequestBody OrderRequestDTO orderRequest) {
        OrderEntity order = orderService.createOrder(orderRequest);
        return Map.of("message", "สร้างออเดอร์สำเร็จ", "orderId", order.getId());
    }

    @GetMapping("/status/{orderId}")
    public Map<String, Object> getOrderStatus(@PathVariable Integer orderId) {
        String status = orderService.getOrderStatus(orderId);
        return Map.of("orderId", orderId, "status", status);
    }

    @GetMapping("/by-table/{tableId}")
    public List<OrderEntity> getOrdersByTable(@PathVariable Integer tableId) {
        return orderService.findOrdersByTable(tableId);
    }

    @DeleteMapping("/reset")
    public Map<String, String> resetAllOrders() {
        return orderService.resetAllOrders();
    }

    @GetMapping("/details/by-table/{tableId}")
    public Map<String, Object> getOrderDetailsByTable(@PathVariable Integer tableId) {
        return orderService.getOrderDetailsByTable(tableId);
    }

    @GetMapping("/bill/{tableId}")
    public Map<String, Object> getBillByTable(@PathVariable Integer tableId) {
        return orderService.getBillByTable(tableId);
    }

    @PostMapping("/reset/{tableId}")
    public Map<String, Object> clearOrdersByTable(@PathVariable Integer tableId) {
        return orderService.clearOrdersByTable(tableId);
    }
}
