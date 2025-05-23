package th.co.priorsolution.training.restaurant.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.priorsolution.training.restaurant.entity.OrderEntity;
import th.co.priorsolution.training.restaurant.entity.OrderItemEntity;
import th.co.priorsolution.training.restaurant.repository.OrderItemRepository;
import th.co.priorsolution.training.restaurant.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class WaitressService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private RestaurantTableService tableService;

    public List<Map<String, Object>> getOrdersByTable(Integer tableId) {
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

    public String calculateBill(Integer tableId) {
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

    @Transactional
    public Map<String, String> resetTable(Integer tableId) {
        List<OrderEntity> orders = orderRepository.findByTableId(tableId);
        for (OrderEntity order : orders) {
            orderItemRepository.deleteByOrder_Id(order.getId());
            orderRepository.delete(order);
        }

        tableService.setStatus(tableId, "AVAILABLE");

        return Map.of("message", "ล้างออเดอร์และเคลียร์โต๊ะเรียบร้อยแล้ว");
    }
}

