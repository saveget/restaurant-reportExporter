package th.co.priorsolution.training.restaurant.model.DTO;

import lombok.Data;

@Data
public class CreateOrderResponse {
    private String message;
    private Integer orderId;

    // constructor, getter, setter
    public CreateOrderResponse(String message, Integer orderId) {
        this.message = message;
        this.orderId = orderId;
    }

}
