package th.co.priorsolution.training.restaurant.model.DTO;

import lombok.Data;

import java.util.List;

@Data
public class OrderResponseDTO {
    private Integer orderId;
    private Integer tableId;
    private String status;
    private List<OrderItemResponseDTO> items;
}