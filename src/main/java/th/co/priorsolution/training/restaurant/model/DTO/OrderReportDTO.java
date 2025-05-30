package th.co.priorsolution.training.restaurant.model.DTO;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderReportDTO {
    private Integer orderId;
    private String tableName;
    private String status;
    private LocalDateTime orderTime;
    private LocalDateTime serveTime;
    private double totalAmount;
    private List<OrderItemReportDTO> items;
}

