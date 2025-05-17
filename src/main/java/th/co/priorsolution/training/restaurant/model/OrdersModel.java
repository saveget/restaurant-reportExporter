package th.co.priorsolution.training.restaurant.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class OrdersModel {
    private int id;
    private int tableId ;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate orderTime;

    private  String status;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate serveTime;
}
