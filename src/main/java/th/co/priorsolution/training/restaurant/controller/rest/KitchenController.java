package th.co.priorsolution.training.restaurant.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import th.co.priorsolution.training.restaurant.entity.OrderItemEntity;
import th.co.priorsolution.training.restaurant.repository.OrderItemRepository;
import th.co.priorsolution.training.restaurant.repository.OrderRepository;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/kitchen")
@CrossOrigin(origins = "*")
public class KitchenController {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/orders")
    public List<OrderItemEntity> getOrdersByStation(@RequestParam String station) {
        return orderItemRepository.findByStationAndStatusIn(station, List.of("pending", "preparing"));
    }

    @PutMapping("/orders/{id}/status")
    public Map<String, Object> updateOrderItemStatus(@PathVariable Integer id,
                                                     @RequestBody Map<String, String> body) {

        String status = body.get("status");
        if (!List.of("pending", "preparing", "done").contains(status)) {
            throw new RuntimeException("สถานะไม่ถูกต้อง");
        }

        OrderItemEntity item = orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบ OrderItem id " + id));

        item.setStatus(status);
        orderItemRepository.save(item);

        // เช็กว่า order หลักต้องเปลี่ยนสถานะไหม
        Integer orderId = item.getOrder().getId();
        List<OrderItemEntity> items = orderItemRepository.findByOrder_Id(orderId);

        boolean allDone = items.stream().allMatch(i -> i.getStatus().equals("done"));
        boolean anyPreparing = items.stream().anyMatch(i -> i.getStatus().equals("preparing"));

        var order = item.getOrder(); // จากความสัมพันธ์ใน OrderItemEntity
        if (allDone) {
            order.setStatus("done");
        } else if (anyPreparing) {
            order.setStatus("preparing");
        } else {
            order.setStatus("pending");
        }

        orderRepository.save(order); // อัปเดต order หลักด้วย

        return Map.of("message", "อัพเดตสถานะเรียบร้อย", "orderItemId", id, "status", status);
    }
}
