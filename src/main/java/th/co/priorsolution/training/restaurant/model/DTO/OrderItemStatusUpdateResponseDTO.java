package th.co.priorsolution.training.restaurant.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemStatusUpdateResponseDTO {
    private String message;
    private Integer orderItemId;
    private String status;
}