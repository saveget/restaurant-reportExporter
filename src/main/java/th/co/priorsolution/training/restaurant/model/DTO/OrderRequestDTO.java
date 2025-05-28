package th.co.priorsolution.training.restaurant.model.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDTO {

    @NotNull(message = "tableId ต้องไม่เป็นค่าว่าง")
    private Integer tableId;

    @NotEmpty(message = "items ต้องมีอย่างน้อย 1 รายการ")
    private List<@NotNull OrderItemRequestDTO> items;
}
