package th.co.priorsolution.training.restaurant.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.priorsolution.training.restaurant.entity.OrderItemEntity;
import th.co.priorsolution.training.restaurant.entity.RestaurantTableEntity;
import th.co.priorsolution.training.restaurant.repository.OrderItemRepository;
import th.co.priorsolution.training.restaurant.repository.RestaurantTableRepository;

import java.util.List;

@Service
public class RestaurantTableService {

    private final RestaurantTableRepository tableRepository;
    @Autowired
    private final OrderItemRepository orderItemRepository;

    public RestaurantTableService(RestaurantTableRepository tableRepository,
                                  OrderItemRepository orderItemRepository) {
        this.tableRepository = tableRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public List<RestaurantTableEntity> getAllTables() {
        return tableRepository.findAll();
    }

    public void toggleStatus(Integer tableId) {
        RestaurantTableEntity table = tableRepository.findById(tableId)
                .orElseThrow(() -> new RuntimeException("ไม่พบโต๊ะที่ระบุ"));

        if ("available".equals(table.getStatus())) {
            table.setStatus("occupied");
        } else {
            table.setStatus("available");
        }

        tableRepository.save(table);
    }

    @Autowired
    private RestaurantTableRepository RestaurantTableRepository;

    @Transactional
    public void setStatus(Integer tableId, String newStatus) {
        RestaurantTableEntity table = tableRepository.findById(tableId)
                .orElseThrow(() -> new RuntimeException("ไม่พบโต๊ะ id " + tableId));

        table.setStatus(newStatus);
        tableRepository.save(table);  // <== สำคัญ ต้องบันทึก!
    }
}


