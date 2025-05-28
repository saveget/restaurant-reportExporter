package th.co.priorsolution.training.restaurant.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import th.co.priorsolution.training.restaurant.entity.RestaurantTableEntity;
import th.co.priorsolution.training.restaurant.repository.OrderItemRepository;
import th.co.priorsolution.training.restaurant.repository.RestaurantTableRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RestaurantTableServiceTest {

    @Mock
    private RestaurantTableRepository tableRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private RestaurantTableService restaurantTableService;

    //ทดสอบว่าถ้าเรียก restaurantTableService.getAllTables() แล้วมันส่งคืนรายการโต๊ะอาหารทั้งหมดตามที่ tableRepository.findAll() ให้มาไหม
    @Test
    void getAllTables_shouldReturnAllTables() {
        // Arrange
        RestaurantTableEntity table1 = new RestaurantTableEntity();
        table1.setId(1);
        table1.setStatus("AVAILABLE");

        RestaurantTableEntity table2 = new RestaurantTableEntity();
        table2.setId(2);
        table2.setStatus("OCCUPIED");

        when(tableRepository.findAll()).thenReturn(Arrays.asList(table1, table2));

        // Act
        List<RestaurantTableEntity> result = restaurantTableService.getAllTables();

        // Assert
        assertEquals(2, result.size());
        verify(tableRepository, times(1)).findAll();
    }

}
