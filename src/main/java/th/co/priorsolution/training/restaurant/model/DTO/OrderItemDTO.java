package th.co.priorsolution.training.restaurant.model.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemDTO {

    @NotNull(message = "menuItemId ต้องไม่เป็นค่าว่าง")
    private Integer menuItemId;

    @Min(value = 1, message = "quantity ต้องมากกว่าหรือเท่ากับ 1")
    private int quantity;
}
