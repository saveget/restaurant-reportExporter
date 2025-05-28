package th.co.priorsolution.training.restaurant.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import th.co.priorsolution.training.restaurant.entity.OrderEntity;
import th.co.priorsolution.training.restaurant.entity.OrderItemEntity;
import th.co.priorsolution.training.restaurant.entity.MenuItemEntity;
import th.co.priorsolution.training.restaurant.repository.OrderItemRepository;
import th.co.priorsolution.training.restaurant.repository.OrderRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WaitressServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private RestaurantTableService tableService;

    @InjectMocks
    private WaitressService waitressService;

    private OrderEntity order1;
    private OrderItemEntity item1;
    private MenuItemEntity menuItem1;

    @BeforeEach
    void setup() {
        menuItem1 = new MenuItemEntity();
        menuItem1.setName("Spaghetti Bolognese");
        menuItem1.setPrice(135.0);

        item1 = new OrderItemEntity();
        item1.setMenuItem(menuItem1);
        item1.setQuantity(2);

        order1 = new OrderEntity();
        order1.setId(1);
    }

    @Test
    void getOrdersByTable_shouldReturnListOfOrderItems() {
        // Arrange
        when(orderRepository.findByTableId(10)).thenReturn(List.of(order1));
        when(orderItemRepository.findByOrder_Id(1)).thenReturn(List.of(item1));

        // Act
        List<Map<String, Object>> result = waitressService.getOrdersByTable(10);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Spaghetti Bolognese", result.get(0).get("menuName"));
        assertEquals(2, result.get(0).get("quantity"));

        verify(orderRepository).findByTableId(10);
        verify(orderItemRepository).findByOrder_Id(1);
    }

    @Test
    void calculateBill_shouldReturnCorrectTotal() {
        // Arrange
        when(orderRepository.findByTableId(10)).thenReturn(List.of(order1));
        when(orderItemRepository.findByOrder_Id(1)).thenReturn(List.of(item1));

        // Act
        String total = waitressService.calculateBill(10);

        // Assert
        assertEquals("270.00", total); // 2 * 135.0 = 240.0

        verify(orderRepository).findByTableId(10);
        verify(orderItemRepository).findByOrder_Id(1);
    }

    @Test
    void resetTable_shouldDeleteOrdersAndSetTableAvailable() {
        // Arrange
        when(orderRepository.findByTableId(10)).thenReturn(List.of(order1));

        // Act
        Map<String, String> response = waitressService.resetTable(10);

        // Assert
        verify(orderItemRepository).deleteByOrder_Id(1);
        verify(orderRepository).delete(order1);
        verify(tableService).setStatus(10, "AVAILABLE");

        assertEquals("ล้างออเดอร์และเคลียร์โต๊ะเรียบร้อยแล้ว", response.get("message"));
    }
}

