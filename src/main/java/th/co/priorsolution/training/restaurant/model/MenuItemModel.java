package th.co.priorsolution.training.restaurant.model;

import lombok.Data;

@Data
public class MenuItemModel {
    private int id;
    private String name;
    private  String category;
    private float price;
    private boolean isAvailable;
}
