package dao;

import db.DataSource;
import entity.DishOrder;
import entity.OrderStatus;
import entity.Status;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDao {
    private final DataSource dataSource = new DataSource();

    public List<OrderStatus> getAllOrderStatusInOrder(int referenceOrder) {
        String sql = "select os.status, os.date_order_status from order_status os inner join \"order\" o" +
                " on os.reference_order = o.reference_order where o.reference_order = ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, referenceOrder);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                List<OrderStatus> orderStatusList = mapOrderStatusResultset(resultSet);
                return orderStatusList;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void changeStatusOrder(List<DishOrder> dishOrders, int referenceOrder) {
        boolean isCreated = dishOrders.stream()
                .allMatch(dishOrder -> dishOrder.getStatusDishOrder().equals(Status.CREATED));
        boolean isConfirmed = dishOrders.stream()
                .allMatch(dishOrder -> dishOrder.getStatusDishOrder().equals(Status.CONFIRMED));
        boolean isInPreparation = dishOrders.stream()
                .allMatch(dishOrder -> dishOrder.getStatusDishOrder().equals(Status.IN_PREPARATION));
        boolean isCompleted = dishOrders.stream()
                .allMatch(dishOrder -> dishOrder.getStatusDishOrder().equals(Status.COMPLETED));
        boolean isServed = dishOrders.stream()
                .allMatch(dishOrder -> dishOrder.getStatusDishOrder().equals(Status.SERVED));

        if (isCreated) {
            insert(Status.CONFIRMED, referenceOrder);
        } else if (isConfirmed) {
            insert(Status.IN_PREPARATION, referenceOrder);
        } else if (isInPreparation) {
            insert(Status.COMPLETED, referenceOrder);
        } else if (isCompleted) {
            insert(Status.SERVED, referenceOrder);
        }  else if (isServed) {
            throw new IllegalArgumentException("All of dish in order is already served");
        }
    }


    private List<OrderStatus> mapOrderStatusResultset(ResultSet resultSet) throws SQLException {
        List<OrderStatus> orderStatusList = new ArrayList<>();
        while (resultSet.next()) {
            OrderStatus orderStatus = new OrderStatus();
            orderStatus.setOrderStatus(Status.valueOf(resultSet.getString("status")));
            orderStatus.setDateOrderStatus(resultSet.getTimestamp("date_order_status"));
            orderStatusList.add(orderStatus);
        }
        return orderStatusList;
    }

    private void insert(Status status, int referenceOrder) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into order_status values (?,?,?)"
            )){
            preparedStatement.setString(1, status.name());
            preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setInt(3, referenceOrder);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
