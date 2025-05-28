package th.co.priorsolution.training.restaurant.service;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import th.co.priorsolution.training.restaurant.entity.*;
import th.co.priorsolution.training.restaurant.model.DTO.*;
import th.co.priorsolution.training.restaurant.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RestaurantTableRepository tableRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder_WithValidRequest_CreatesOrderSuccessfully() {
        // Arrange
        RestaurantTableEntity table = new RestaurantTableEntity();
        table.setId(1);

        MenuItemEntity menuItem = new MenuItemEntity();
        menuItem.setId(401);
        menuItem.setCategory("beverage");

        OrderItemRequestDTO itemDTO = new OrderItemRequestDTO();
        itemDTO.setMenuItemId(10);
        itemDTO.setQuantity(2);

        OrderRequestDTO request = new OrderRequestDTO();
        request.setTableId(1);
        request.setItems(List.of(itemDTO));

        when(tableRepository.findById(1)).thenReturn(Optional.of(table));
        when(menuItemRepository.findById(10)).thenReturn(Optional.of(menuItem));
        when(orderRepository.save(any(OrderEntity.class))).thenAnswer(invocation -> {
            OrderEntity order = invocation.getArgument(0);
            order.setId(10); // mock ID assigned by DB
            return order;
        });
        when(orderItemRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        OrderEntity createdOrder = orderService.createOrder(request);

        // Assert
        assertNotNull(createdOrder);
        assertEquals(10, createdOrder.getId());
        assertEquals(table, createdOrder.getTable());
        assertEquals("pending", createdOrder.getStatus());
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
        verify(orderItemRepository, times(1)).saveAll(anyList());
    }

    @Test
    void createOrder_WithEmptyItems_ThrowsException() {
        // Arrange
        OrderRequestDTO request = new OrderRequestDTO();
        request.setTableId(1);
        request.setItems(List.of()); // empty

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.createOrder(request);
        });

        assertEquals("ไม่สามารถสร้างคำสั่งซื้อที่ไม่มีรายการอาหารได้", exception.getMessage());
    }

    @Test
    void getOrderStatus_ExistingOrder_ReturnsStatus() {
        // Arrange
        OrderEntity order = new OrderEntity();
        order.setId(1);
        order.setStatus("done");

        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        // Act
        String status = orderService.getOrderStatus(1);

        // Assert
        assertEquals("done", status);
    }

    @Test
    void getOrderStatus_OrderNotFound_ThrowsException() {
        // Arrange
        when(orderRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.getOrderStatus(999);
        });

        assertEquals("Order not found", exception.getMessage());
    }

    @Test
    void updateOrderStatus_ExistingOrder_UpdatesStatus() {
        // Arrange
        OrderEntity order = new OrderEntity();
        order.setId(1);
        order.setStatus("pending");

        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        // Act
        orderService.updateOrderStatus(1, "done");

        // Assert
        assertEquals("done", order.getStatus());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void deleteOrderItem_ExistingOrderItem_ReturnsTrue() {
        // Arrange
        when(orderItemRepository.existsById(10)).thenReturn(true);

        // Act
        boolean result = orderService.deleteOrderItem(10);

        // Assert
        assertTrue(result);
        verify(orderItemRepository, times(1)).deleteById(10);
    }

    @Test
    void deleteOrderItem_NonExistingOrderItem_ReturnsFalse() {
        // Arrange
        when(orderItemRepository.existsById(20)).thenReturn(false);

        // Act
        boolean result = orderService.deleteOrderItem(20);

        // Assert
        assertFalse(result);
        verify(orderItemRepository, never()).deleteById(anyInt());
    }

}

