package th.co.priorsolution.training.restaurant.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import th.co.priorsolution.training.restaurant.entity.RestaurantTableEntity;
import th.co.priorsolution.training.restaurant.repository.OrderItemRepository;
import th.co.priorsolution.training.restaurant.service.RestaurantTableService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tables")
@CrossOrigin(origins = "*")
public class RestaurantTableController {

    private final RestaurantTableService tableService;

    public RestaurantTableController(RestaurantTableService tableService) {
        this.tableService = tableService;
    }

    @GetMapping
    public List<RestaurantTableEntity> getAllTables() {
        return tableService.getAllTables();
    }

    @PostMapping("/toggle-status/{tableId}")
    public Map<String, String> toggleTableStatus(@PathVariable Integer tableId) {
        try {
            tableService.toggleStatus(tableId);
            return Map.of("message", "เปลี่ยนสถานะโต๊ะเรียบร้อยแล้ว");
        } catch (Exception e) {
            return Map.of("message", "ไม่สามารถเปลี่ยนสถานะโต๊ะได้: " + e.getMessage());
        }
    }
}
