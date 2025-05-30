package th.co.priorsolution.training.restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.priorsolution.training.restaurant.entity.OrderItemEntity;
import th.co.priorsolution.training.restaurant.model.DTO.OrderItemResponseDTO;
import th.co.priorsolution.training.restaurant.model.DTO.OrderItemStatusUpdateResponseDTO;
import th.co.priorsolution.training.restaurant.repository.OrderItemRepository;
import th.co.priorsolution.training.restaurant.repository.OrderRepository;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class KitchenService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    public List<OrderItemResponseDTO> getOrdersByStation(String station) {
        return orderItemRepository.findByStationAndStatusIn(station, List.of("pending", "preparing"))
                .stream()
                .map(item -> new OrderItemResponseDTO(
                        item.getId(),
                        item.getMenuItem().getName(),
                        item.getQuantity(),
                        item.getStatus(),
                        item.getStation()
                ))
                .toList();
    }


    public OrderItemStatusUpdateResponseDTO updateOrderItemStatus(Integer id, String status) {
        if (!List.of("pending", "preparing", "done").contains(status)) {
            throw new IllegalArgumentException("สถานะไม่ถูกต้อง");
        }

        OrderItemEntity item = orderItemRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("ไม่พบ OrderItem id " + id));

        item.setStatus(status);
        orderItemRepository.save(item);

        Integer orderId = item.getOrder().getId();
        List<OrderItemEntity> items = orderItemRepository.findByOrder_Id(orderId);

        boolean allDone = items.stream().allMatch(i -> i.getStatus().equals("done"));
        boolean anyPreparing = items.stream().anyMatch(i -> i.getStatus().equals("preparing"));

        var order = item.getOrder();
        if (allDone) {
            order.setStatus("done");
            if (order.getServeTime() == null) {
                order.setServeTime(java.time.LocalDateTime.now());
            }
        } else if (anyPreparing) {
            order.setStatus("preparing");
        } else {
            order.setStatus("pending");
        }

        orderRepository.save(order);

        return new OrderItemStatusUpdateResponseDTO("อัพเดตสถานะเรียบร้อย", id, status);
    }

}
