package th.co.priorsolution.training.restaurant.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.priorsolution.training.restaurant.entity.MenuItemEntity;
import th.co.priorsolution.training.restaurant.entity.OrderEntity;
import th.co.priorsolution.training.restaurant.entity.OrderItemEntity;
import th.co.priorsolution.training.restaurant.entity.RestaurantTableEntity;
import th.co.priorsolution.training.restaurant.model.DTO.OrderItemDTO;
import th.co.priorsolution.training.restaurant.model.DTO.OrderRequestDTO;
import th.co.priorsolution.training.restaurant.repository.MenuItemRepository;
import th.co.priorsolution.training.restaurant.repository.OrderItemRepository;
import th.co.priorsolution.training.restaurant.repository.OrderRepository;
import th.co.priorsolution.training.restaurant.repository.RestaurantTableRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestaurantTableRepository tableRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    public OrderEntity createOrder(OrderRequestDTO orderRequest) {
        if (orderRequest.getItems() == null || orderRequest.getItems().isEmpty()) {
            throw new RuntimeException("ไม่สามารถสร้างคำสั่งซื้อที่ไม่มีรายการอาหารได้");
        }

        System.out.println("สร้างออเดอร์ใหม่ - โต๊ะ: " + orderRequest.getTableId());
        System.out.println("จำนวนเมนู: " + orderRequest.getItems().size());

        // หาโต๊ะจาก tableId
        RestaurantTableEntity table = tableRepository.findById(orderRequest.getTableId())
                .orElseThrow(() -> new RuntimeException("ไม่พบโต๊ะ id " + orderRequest.getTableId()));

        // สร้าง OrderEntity ใหม่
        OrderEntity order = new OrderEntity();
        order.setTable(table);
        order.setOrderTime(LocalDateTime.now());
        order.setStatus("pending");

        // บันทึก order ก่อน เพื่อให้มี id (ถ้าใช้ GeneratedValue)
        order = orderRepository.save(order);

        // สร้าง OrderItemEntities
        List<OrderItemEntity> orderItems = new ArrayList<>();

        for (OrderItemDTO itemDTO : orderRequest.getItems()) {
            System.out.println("กำลังโหลดเมนู id: " + itemDTO.getMenuItemId());

            MenuItemEntity menuItem = menuItemRepository.findById(itemDTO.getMenuItemId())
                    .orElseThrow(() -> new RuntimeException("ไม่พบเมนู id " + itemDTO.getMenuItemId()));

            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setOrder(order);
            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setStation(menuItem.getCategory());
            orderItem.setStatus("pending");
            orderItems.add(orderItem);
        }

        // บันทึก orderItems ทั้งหมด
        orderItemRepository.saveAll(orderItems);


        return order;
    }


    public String getOrderStatus(Integer orderId) {
        // ตรวจสอบว่ามีโค้ดแบบนี้อยู่ไหม:
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return order.getStatus();
    }

    public List<OrderItemEntity> getOrderItemsByStationAndStatuses(String station, List<String> statuses) {
        return orderItemRepository.findByStationAndStatusIn(station, statuses);
    }

    public List<OrderEntity> findOrdersByTable(Integer tableId) {
        return orderRepository.findByTableId(tableId);
    }

    @Transactional
    public void updateOrderStatus(Integer orderId, String newStatus) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(newStatus);
        orderRepository.save(order); // สำคัญ!!
    }



    @Transactional
    public boolean deleteOrderItem(Integer orderItemId) {
        if (orderItemRepository.existsById(orderItemId)) {
            orderItemRepository.deleteById(orderItemId);
            return true;
        }
        return false;
    }






}


