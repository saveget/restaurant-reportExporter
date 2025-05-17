package th.co.priorsolution.training.restaurant.model;

import lombok.Data;

@Data
public class OrderItemRequest {
    private String name;
    private Double price;
    private Integer quantity;
}
