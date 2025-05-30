package th.co.priorsolution.training.restaurant.model.DTO;

import lombok.Data;

@Data
public class OrderItemReportDTO {
    private String menuName;
    private int quantity;
    private double price;
    private String status;
    private String station;
    private double subtotal;

}