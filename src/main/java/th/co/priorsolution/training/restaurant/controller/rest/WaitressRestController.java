package th.co.priorsolution.training.restaurant.controller.rest;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import th.co.priorsolution.training.restaurant.entity.OrderEntity;
import th.co.priorsolution.training.restaurant.entity.OrderItemEntity;
import th.co.priorsolution.training.restaurant.repository.OrderItemRepository;
import th.co.priorsolution.training.restaurant.repository.OrderRepository;
import th.co.priorsolution.training.restaurant.service.RestaurantTableService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/waitress")
@CrossOrigin(origins = "*")
public class WaitressRestController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @GetMapping("/orders/{tableId}")
    public List<Map<String, Object>> getOrdersByTable(@PathVariable Integer tableId) {
        List<OrderEntity> orders = orderRepository.findByTableId(tableId);
        List<Map<String, Object>> result = new ArrayList<>();

        for (OrderEntity order : orders) {
            List<OrderItemEntity> items = orderItemRepository.findByOrder_Id(order.getId());
            for (OrderItemEntity item : items) {
                result.add(Map.of(
                        "menuName", item.getMenuItem().getName(),
                        "quantity", item.getQuantity()
                ));
            }
        }

        return result;
    }

    @GetMapping("/bill/{tableId}")
    public String getBill(@PathVariable Integer tableId) {
        List<OrderEntity> orders = orderRepository.findByTableId(tableId);
        double total = 0;

        for (OrderEntity order : orders) {
            List<OrderItemEntity> items = orderItemRepository.findByOrder_Id(order.getId());
            total += items.stream()
                    .mapToDouble(i -> i.getQuantity() * i.getMenuItem().getPrice())
                    .sum();
        }

        return String.format("%.2f", total);
    }

//    @PostMapping("/reset/{tableId}")
//    public Map<String, String> resetTable(@PathVariable Integer tableId) {
//        List<OrderEntity> orders = orderRepository.findByTableId(tableId);
//        for (OrderEntity order : orders) {
//            orderItemRepository.deleteByOrder_Id(order.getId());
//            orderRepository.delete(order);
//        }
//        return Map.of("message", "ล้างออเดอร์โต๊ะแล้ว");
//    }

    @Autowired
    private RestaurantTableService tableService;  // เพิ่มถ้ามี service จัดการโต๊ะ

    @PostMapping("/reset/{tableId}")
    @Transactional
    public Map<String, String> resetTable(@PathVariable Integer tableId) {
        List<OrderEntity> orders = orderRepository.findByTableId(tableId);
        for (OrderEntity order : orders) {
            orderItemRepository.deleteByOrder_Id(order.getId());
            orderRepository.delete(order);
        }

        tableService.setStatus(tableId, "available");  // เรียกเปลี่ยนสถานะ

        return Map.of("message", "ล้างออเดอร์และเคลียร์โต๊ะเรียบร้อยแล้ว");
    }

}

