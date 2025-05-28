package th.co.priorsolution.training.restaurant.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemResponseDTO {
    private Integer id;
    private String menuName;
    private int quantity;
    private String status;
    private String station;
}
