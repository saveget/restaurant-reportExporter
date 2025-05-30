package th.co.priorsolution.training.restaurant.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.priorsolution.training.restaurant.entity.MenuItemEntity;
import th.co.priorsolution.training.restaurant.entity.OrderEntity;
import th.co.priorsolution.training.restaurant.entity.OrderItemEntity;
import th.co.priorsolution.training.restaurant.entity.RestaurantTableEntity;
import th.co.priorsolution.training.restaurant.model.DTO.*;
import th.co.priorsolution.training.restaurant.model.DTO.OrderItemRequestDTO;
import th.co.priorsolution.training.restaurant.repository.MenuItemRepository;
import th.co.priorsolution.training.restaurant.repository.OrderItemRepository;
import th.co.priorsolution.training.restaurant.repository.OrderRepository;
import th.co.priorsolution.training.restaurant.repository.RestaurantTableRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        for (OrderItemRequestDTO itemDTO : orderRequest.getItems()) {
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


    public Map<String, String> resetAllOrders() {
        orderItemRepository.deleteAll();
        orderRepository.deleteAll();
        return Map.of("message", "รีเซ็ตคำสั่งซื้อทั้งหมดเรียบร้อยแล้ว");
    }

    public Map<String, Object> getOrderDetailsByTable(Integer tableId) {
        List<OrderEntity> orders = orderRepository.findByTableId(tableId);
        if (orders.isEmpty()) {
            throw new RuntimeException("ไม่พบคำสั่งซื้อของโต๊ะนี้");
        }

        OrderEntity latestOrder = orders.get(orders.size() - 1);
        var items = orderItemRepository.findByOrder_Id(latestOrder.getId());

        List<Map<String, Object>> itemList = items.stream().map(item -> {
            Map<String, Object> map = Map.of(
                    "menuName", item.getMenuItem().getName(),
                    "price", (Object) item.getMenuItem().getPrice(),
                    "quantity", (Object) item.getQuantity(),
                    "status", item.getStatus(),
                    "subtotal", (Object) (item.getQuantity() * item.getMenuItem().getPrice())
            );
            return map;
        }).collect(Collectors.toList());

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


    public List<OrderResponseDTO> getOrdersResponseByTable(Integer tableId) {
        List<OrderEntity> orders = orderRepository.findByTableId(tableId);
        return orders.stream().map(order -> {
            OrderResponseDTO dto = new OrderResponseDTO();
            dto.setOrderId(order.getId());
            dto.setTableId(order.getTable().getId());
            dto.setStatus(order.getStatus());

            List<OrderItemResponseDTO> itemDTOs = order.getItems().stream().map(item ->
                    new OrderItemResponseDTO(
                            item.getId(),
                            item.getMenuItem().getName(),
                            item.getQuantity(),
                            item.getStatus(),
                            item.getStation()
                    )
            ).collect(Collectors.toList());

            dto.setItems(itemDTOs);
            return dto;
        }).collect(Collectors.toList());
    }

    public List<OrderDetailDTO> getAllOrderDetailsByTable(Integer tableId) {
        List<OrderEntity> orders = orderRepository.findByTableId(tableId);
        if (orders.isEmpty()) {
            throw new RuntimeException("ไม่พบคำสั่งซื้อของโต๊ะนี้");
        }

        return orders.stream().map(order -> {
            List<OrderItemEntity> items = orderItemRepository.findByOrder_Id(order.getId());
            List<OrderItemDTO> itemDTOs = items.stream().map(item -> new OrderItemDTO(
                    item.getMenuItem().getName(),
                    item.getMenuItem().getPrice(),
                    item.getQuantity(),
                    item.getStatus(),
                    item.getQuantity() * item.getMenuItem().getPrice()
            )).collect(Collectors.toList());

            double total = itemDTOs.stream().mapToDouble(OrderItemDTO::getSubtotal).sum();

            return new OrderDetailDTO(
                    order.getId(),
                    order.getStatus(),
                    tableId,
                    itemDTOs,
                    total
            );
        }).collect(Collectors.toList());
    }

    public List<OrderReportDTO> getAllOrderReports() {
        List<OrderEntity> orders = orderRepository.findAll();

        return orders.stream().map(order -> {
            OrderReportDTO dto = new OrderReportDTO();
            dto.setOrderId(order.getId());
            dto.setTableName(order.getTable().getTableNum());  // หรือ getName() ขึ้นกับ entity ของโต๊ะ
            dto.setStatus(order.getStatus());
            dto.setOrderTime(order.getOrderTime());
            dto.setServeTime(order.getServeTime());

            List<OrderItemReportDTO> items = order.getItems().stream().map(item -> {
                OrderItemReportDTO itemDTO = new OrderItemReportDTO();
                itemDTO.setMenuName(item.getMenuItem().getName());
                itemDTO.setQuantity(item.getQuantity());
                itemDTO.setPrice(item.getMenuItem().getPrice());  // เพิ่ม price
                itemDTO.setStatus(item.getStatus());
                itemDTO.setStation(item.getStation());
                itemDTO.setSubtotal(item.getQuantity() * item.getMenuItem().getPrice());  // เพิ่ม subtotal
                return itemDTO;
            }).collect(Collectors.toList());

            dto.setItems(items);

            double totalAmount = items.stream()
                    .mapToDouble(OrderItemReportDTO::getSubtotal)
                    .sum();

            dto.setTotalAmount(totalAmount);

            return dto;
        }).collect(Collectors.toList());
    }











}


