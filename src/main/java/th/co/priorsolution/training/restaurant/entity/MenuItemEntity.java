package th.co.priorsolution.training.restaurant.entity;

import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "menu_items")
@Data
public class MenuItemEntity {
    @Id
    private Integer id;
    @Column(nullable = false)
    private String name;
    private String category;
    private Double price;
    @Column(name = "is_available")
    private Boolean isAvailable;


}
