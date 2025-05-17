package th.co.priorsolution.training.restaurant.model;

import lombok.Data;

@Data
public class TablesModel {
    private int id;
    private String tableNum;
    private String status;
    private int seat;
}
