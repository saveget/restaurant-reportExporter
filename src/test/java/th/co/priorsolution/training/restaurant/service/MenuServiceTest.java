package th.co.priorsolution.training.restaurant.service;



import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import th.co.priorsolution.training.restaurant.entity.MenuItemEntity;
import th.co.priorsolution.training.restaurant.model.DTO.MenuItemDTO;
import th.co.priorsolution.training.restaurant.repository.MenuItemRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private MenuService menuService;

    @Test
    void getAvailableMenuItems_ReturnsListOfAvailableItems() {
        // Arrange
        MenuItemEntity item1 = new MenuItemEntity();
        item1.setId(1);
        item1.setName("Spaghetti Carbonara");
        item1.setCategory("pasta");
        item1.setPrice(130.0);
        item1.setIsAvailable(true);

        MenuItemEntity item2 = new MenuItemEntity();
        item2.setId(2);
        item2.setName("Caesar Salad");
        item2.setCategory("salad");
        item2.setPrice(95.0);
        item2.setIsAvailable(true);

        when(menuItemRepository.findByIsAvailableTrue())
                .thenReturn(List.of(item1, item2));

        // Act
        List<MenuItemDTO> result = menuService.getAvailableMenuItems();

        // Assert
        assertEquals(2, result.size());

        assertEquals("Spaghetti Carbonara", result.get(0).getName());
        assertEquals("pasta", result.get(0).getCategory());
        assertEquals(130, result.get(0).getPrice());
        assertTrue(result.get(0).getIsAvailable());

        assertEquals("Caesar Salad", result.get(1).getName());
        assertEquals("salad", result.get(1).getCategory());
        assertEquals(95, result.get(1).getPrice());
        assertTrue(result.get(1).getIsAvailable());

        verify(menuItemRepository, times(1)).findByIsAvailableTrue();
    }

    @Test
    void getAvailableMenuItems_ReturnsEmptyList_WhenNoAvailableItems() {
        // Arrange
        when(menuItemRepository.findByIsAvailableTrue())
                .thenReturn(List.of());

        // Act
        List<MenuItemDTO> result = menuService.getAvailableMenuItems();

        // Assert
        assertTrue(result.isEmpty());

        verify(menuItemRepository, times(1)).findByIsAvailableTrue();
    }
}

