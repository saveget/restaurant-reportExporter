package th.co.priorsolution.training.restaurant.repository.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.co.priorsolution.training.restaurant.model.OrdersModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderNativeRepositoryImpl {

    private final JdbcTemplate jdbcTemplate;

    public OrderNativeRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<OrdersModel> findOrdersByCondition(OrdersModel ordersModel) {
        List<Object> paramList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT id, table_id, order_time, status, serve_time ");
        sb.append("FROM orders WHERE 1=1 ");

        if (ordersModel.getOrderTime() != null) {
            sb.append("AND order_time = ? ");
            paramList.add(ordersModel.getOrderTime());
        }

        if (ordersModel.getStatus() != null && !ordersModel.getStatus().isEmpty()) {
            sb.append("AND status = ? ");
            paramList.add(ordersModel.getStatus());
        }

        if (ordersModel.getServeTime() != null) {
            sb.append("AND serve_time = ? ");
            paramList.add(ordersModel.getServeTime());
        }

        return jdbcTemplate.query(sb.toString(), paramList.toArray(), new OrdersModelRowMapper());
    }

    // RowMapper แปลง ResultSet เป็น OrdersModel
    private static class OrdersModelRowMapper implements RowMapper<OrdersModel> {
        @Override
        public OrdersModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            OrdersModel model = new OrdersModel();
            model.setId(rs.getInt("id"));
            model.setTableId(rs.getInt("table_id"));
            model.setOrderTime(LocalDate.from(rs.getTimestamp("order_time").toLocalDateTime()));
            model.setStatus(rs.getString("status"));
            model.setServeTime(rs.getTimestamp("serve_time") != null ? LocalDate.from(rs.getTimestamp("serve_time").toLocalDateTime()) : null);
            return model;
        }
    }
}
