package th.co.priorsolution.training.restaurant.model;

import lombok.Data;

@Data
public class OrderItemModel {
    private int id;
    private  int orderId;
    private int menuItemID;
    private int quantity;
    private String status;
}
