package th.co.priorsolution.training.restaurant.service;

import org.springframework.stereotype.Service;
import th.co.priorsolution.training.restaurant.entity.MenuItemEntity;
import th.co.priorsolution.training.restaurant.model.MenuItemModel;
import th.co.priorsolution.training.restaurant.repository.MenuItemRepository;
import java.util.List;
import java.awt.*;

@Service
public class MenuService {

    private final MenuItemRepository menuItemRepository;

    public MenuService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public List<MenuItemEntity> getAvailableMenuItems() {
        return menuItemRepository.findByIsAvailableTrue();
    }

}
