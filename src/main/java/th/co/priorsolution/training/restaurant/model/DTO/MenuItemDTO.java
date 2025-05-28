package th.co.priorsolution.training.restaurant.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemDTO {
    private Integer id;
    private String name;
    private String category;  // ใส่ถ้าต้องการแสดงหมวดหมู่ด้วย
    private Double price;
    private Boolean isAvailable;  // ใส่ถ้าต้องการบอกว่าเมนูนี้ยังใช้ได้อยู่ไหม
}

