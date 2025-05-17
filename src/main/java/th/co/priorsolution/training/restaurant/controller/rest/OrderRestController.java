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

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;


    @PostMapping
    public Map<String, Object> createOrder(@RequestBody OrderRequestDTO orderRequest) {
        OrderEntity order = orderService.createOrder(orderRequest);
        return Map.of("message", "สร้างออเดอร์สำเร็จ", "orderId", order.getId());
    }

    @GetMapping("/status/{orderId}")
    public Map<String, Object> getOrderStatus(@PathVariable Integer orderId) {
        System.out.println("เรียกสถานะ orderId: " + orderId);
        String status = orderService.getOrderStatus(orderId);
        System.out.println("สถานะ orderId " + orderId + " = " + status);
        return Map.of("orderId", orderId, "status", status);
    }


    // เปลี่ยนจาก @RequestParam เป็น @PathVariable
    @GetMapping("/by-table/{tableId}")
    public List<OrderEntity> getOrdersByTable(@PathVariable Integer tableId) {
        return orderService.findOrdersByTable(tableId);
    }

    @DeleteMapping("/reset")
    public Map<String, String> resetAllOrders() {
        orderItemRepository.deleteAll();
        orderRepository.deleteAll();
        return Map.of("message", "รีเซ็ตคำสั่งซื้อทั้งหมดเรียบร้อยแล้ว");
    }

    @GetMapping("/details/by-table/{tableId}")
    public Map<String, Object> getOrderDetailsByTable(@PathVariable Integer tableId) {
        List<OrderEntity> orders = orderRepository.findByTableId(tableId);
        if (orders.isEmpty()) {
            throw new RuntimeException("ไม่พบคำสั่งซื้อของโต๊ะนี้");
        }

        OrderEntity latestOrder = orders.get(orders.size() - 1); // ใช้ order ล่าสุด
        var items = orderItemRepository.findByOrder_Id(latestOrder.getId());



        List<Map<String, Object>> itemList = items.stream().map(item -> Map.<String, Object>of(
                "menuName", item.getMenuItem().getName(),
                "price", item.getMenuItem().getPrice(),
                "quantity", item.getQuantity(),
                "status", item.getStatus(),
                "subtotal", item.getQuantity() * item.getMenuItem().getPrice()
        )).collect(Collectors.toList());


        double total = items.stream()
                .mapToDouble(item -> item.getQuantity() * item.getMenuItem().getPrice())
                .sum();

        return Map.of(
                "orderId", latestOrder.getId(),
                "status", latestOrder.getStatus(),
                "tableId", tableId,
                "items", itemList,
                "total", total
        );
    }

    @GetMapping("/bill/{tableId}")
    public Map<String, Object> getBillByTable(@PathVariable Integer tableId) {
        List<OrderEntity> orders = orderRepository.findByTableId(tableId);
        double total = orders.stream()
                .flatMap(order -> order.getItems().stream())
                .mapToDouble(item -> item.getMenuItem().getPrice() * item.getQuantity())
                .sum();

        return Map.of("tableId", tableId, "total", total);
    }

    @PostMapping("/reset/{tableId}")
    public Map<String, Object> clearOrdersByTable(@PathVariable Integer tableId) {
        List<OrderEntity> orders = orderRepository.findByTableId(tableId);
        for (OrderEntity order : orders) {
            orderItemRepository.deleteAll(order.getItems());
        }
        orderRepository.deleteAll(orders);

        return Map.of("message", "เคลียร์ออเดอร์ของโต๊ะ " + tableId + " แล้ว");
    }





}
