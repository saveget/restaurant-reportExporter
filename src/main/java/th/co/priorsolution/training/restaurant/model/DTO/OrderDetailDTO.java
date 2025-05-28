package th.co.priorsolution.training.restaurant.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderDetailDTO {
    private Integer orderId;
    private String status;
    private Integer tableId;
    private List<OrderItemDTO> items;
    private double total;
}
