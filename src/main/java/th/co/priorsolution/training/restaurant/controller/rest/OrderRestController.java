package th.co.priorsolution.training.restaurant.controller.rest;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.co.priorsolution.training.restaurant.entity.OrderEntity;
import th.co.priorsolution.training.restaurant.model.DTO.*;
import th.co.priorsolution.training.restaurant.repository.OrderItemRepository;
import th.co.priorsolution.training.restaurant.repository.OrderRepository;
import th.co.priorsolution.training.restaurant.service.OrderService;

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
    public ResponseEntity<CreateOrderResponse> createOrder(@Valid @RequestBody OrderRequestDTO orderRequest) {
        OrderEntity order = orderService.createOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateOrderResponse("สร้างออเดอร์สำเร็จ", order.getId()));
    }


    @GetMapping("/status/{orderId}")
    public OrderStatusResponse getOrderStatus(@PathVariable Integer orderId) {
        String status = orderService.getOrderStatus(orderId);
        return new OrderStatusResponse(orderId, status);
    }


    @GetMapping("/by-table/{tableId}")
    public List<OrderResponseDTO> getOrdersByTable(@PathVariable Integer tableId) {
        return orderService.getOrdersResponseByTable(tableId);
    }


    @DeleteMapping("/reset")
    public Map<String, String> resetAllOrders() {
        return orderService.resetAllOrders();
    }

    @GetMapping("/details/by-table/{tableId}")
    public List<OrderDetailDTO> getAllOrderDetailsByTable(@PathVariable Integer tableId) {
        return orderService.getAllOrderDetailsByTable(tableId);
    }


}
