package th.co.priorsolution.training.restaurant.model;

import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {
    private Integer tableId;
    private List<OrderItemRequest> items;
}
