package th.co.priorsolution.training.restaurant.controller.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import th.co.priorsolution.training.restaurant.entity.OrderItemEntity;
import th.co.priorsolution.training.restaurant.service.OrderService;

import java.util.List;

@RestController
public class OrderItemRestController {

    private final OrderService orderService;

    public OrderItemRestController(OrderService orderService) {
        this.orderService = orderService;
    }


}