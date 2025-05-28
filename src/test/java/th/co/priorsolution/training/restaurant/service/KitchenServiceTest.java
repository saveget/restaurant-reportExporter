package th.co.priorsolution.training.restaurant.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import th.co.priorsolution.training.restaurant.entity.MenuItemEntity;
import th.co.priorsolution.training.restaurant.entity.OrderEntity;
import th.co.priorsolution.training.restaurant.entity.OrderItemEntity;
import th.co.priorsolution.training.restaurant.model.DTO.OrderItemResponseDTO;
import th.co.priorsolution.training.restaurant.repository.OrderItemRepository;
import th.co.priorsolution.training.restaurant.repository.OrderRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KitchenServiceTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private KitchenService kitchenService;

    @Test
    void testGetOrdersByStation_ReturnsFilteredOrders() {
        // Arrange
        OrderEntity order = new OrderEntity();
        order.setId(1);

        MenuItemEntity menuItem = new MenuItemEntity();
        menuItem.setName("Grilled Chicken");

        OrderItemEntity mockItem = new OrderItemEntity();
        mockItem.setId(101);
        mockItem.setQuantity(1);
        mockItem.setStatus("pending");
        mockItem.setStation("grill");
        mockItem.setMenuItem(menuItem);
        mockItem.setOrder(order);

        List<String> statuses = List.of("pending", "preparing");

        when(orderItemRepository.findByStationAndStatusIn(eq("grill"), eq(statuses)))
                .thenReturn(List.of(mockItem));

        // Act
        List<OrderItemResponseDTO> result = kitchenService.getOrdersByStation("grill");

        // Assert
        assertEquals(1, result.size());
        assertEquals("Grilled Chicken", result.get(0).getMenuName());
        assertEquals("pending", result.get(0).getStatus());

        verify(orderItemRepository).findByStationAndStatusIn(eq("grill"), eq(statuses));
    }

    @Test
    void updateOrderItemStatus_ValidInput_UpdatesCorrectly() {
        // Arrange
        OrderEntity mockOrder = new OrderEntity();
        mockOrder.setId(1);
        mockOrder.setStatus("pending");

        OrderItemEntity mockItem = new OrderItemEntity();
        mockItem.setId(101);
        mockItem.setStatus("pending");
        mockItem.setOrder(mockOrder);

        OrderItemEntity anotherItem = new OrderItemEntity();
        anotherItem.setId(102);
        anotherItem.setStatus("done");

        when(orderItemRepository.findById(101)).thenReturn(java.util.Optional.of(mockItem));
        when(orderItemRepository.findByOrder_Id(1)).thenReturn(List.of(mockItem, anotherItem));
        when(orderItemRepository.save(any())).thenReturn(mockItem);
        when(orderRepository.save(any())).thenReturn(mockOrder);

        // Act
        var response = kitchenService.updateOrderItemStatus(101, "preparing");

        // Assert
        assertEquals("อัพเดตสถานะเรียบร้อย", response.getMessage());
        assertEquals(101, response.getOrderItemId());
        assertEquals("preparing", response.getStatus());  // <-- แก้ตรงนี้

        verify(orderItemRepository).save(mockItem);
        verify(orderRepository).save(mockOrder);
    }


    @Test
    void updateOrderItemStatus_InvalidStatus_ThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            kitchenService.updateOrderItemStatus(101, "cooking"); // สถานะไม่ถูกต้อง
        });
        assertEquals("สถานะไม่ถูกต้อง", exception.getMessage());
    }

    @Test
    void updateOrderItemStatus_OrderItemNotFound_ThrowsException() {
        when(orderItemRepository.findById(999)).thenReturn(java.util.Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            kitchenService.updateOrderItemStatus(999, "done");
        });
        assertEquals("ไม่พบ OrderItem id 999", exception.getMessage());
    }


}
