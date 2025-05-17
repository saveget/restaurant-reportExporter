package th.co.priorsolution.training.restaurant.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "restaurant_tables")
@Data
public class RestaurantTableEntity {
    @Id
    private Integer id;

    @Column(name = "table_num")
    private String tableNum;

    private String status;

    private Integer seats;

    @OneToMany(mappedBy = "table")
    @JsonIgnore
    private List<OrderEntity> orders;
}