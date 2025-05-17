package th.co.priorsolution.training.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import th.co.priorsolution.training.restaurant.entity.MenuItemEntity;

import java.awt.*;
import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItemEntity, Integer> {
    List<MenuItemEntity> findByIsAvailableTrue(); // ดึงเฉพาะเมนูที่ยังเปิดให้สั่ง
}
