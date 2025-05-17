package th.co.priorsolution.training.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import th.co.priorsolution.training.restaurant.entity.OrderEntity;
import th.co.priorsolution.training.restaurant.entity.OrderItemEntity;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Integer> {
    List<OrderItemEntity> findByStationAndStatusIn(String station, List<String> statuses);
    List<OrderItemEntity> findByOrder_Id(Integer orderId);

    void deleteByOrder_Id(Integer orderId);
}
