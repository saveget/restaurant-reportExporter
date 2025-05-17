package th.co.priorsolution.training.restaurant.model.DTO;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDTO {
    private Integer tableId;
    private List<OrderItemDTO> items;
}

