package dao;

import db.DataSource;
import entity.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDao {
    private final DataSource dataSource = new DataSource();
    private final DishOrderDao dishOrderDao = new DishOrderDao();

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

    public void addDishOrderInOrderByIdDish(DishOrder dishOrder) {
        int availableDish = dishOrder.getDish().getAvailableDish();
        if (availableDish < dishOrder.getQuantityOfDish()) {
            throw new IllegalArgumentException("We have just : "+availableDish+" "+dishOrder.getDish().getName());
        }
        String sql = "insert into dish_order values (?,  ?, ?, ?)";
        String sqlStatus = "insert into dish_order_status values (?, ?, ?, ?)";

        try(Connection connection = dataSource.getConnection()) {
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, dishOrder.getIdDishOrder());
                preparedStatement.setInt(2, dishOrder.getDish().getId_dish());
                preparedStatement.setInt(3, dishOrder.getQuantityOfDish());
                preparedStatement.setInt(4, dishOrder.getReferenceOrder());

                preparedStatement.executeUpdate();
            }

            try(PreparedStatement preparedStatement2 = connection.prepareStatement(sqlStatus)) {
                preparedStatement2.setObject(1, Status.CREATED, Types.OTHER);
                preparedStatement2.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                preparedStatement2.setInt(3, dishOrder.getIdDishOrder());
                preparedStatement2.setInt(4, dishOrder.getReferenceOrder());

                preparedStatement2.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Order order) {
        for (DishOrder dishOrder : order.getDishOrders()) {
            int availableDish = dishOrder.getDish().getAvailableDish();
            if(availableDish < dishOrder.getQuantityOfDish()) {
                throw new IllegalArgumentException("We have just : "+availableDish+" "+dishOrder.getDish().getName());
            }
        }

        String sqlOrder = "insert into \"order\" (reference_order) values (?) on conflict do nothing";
        String sqlOrderStatus = "insert into \"order_status\" values (?, ?, ?) ";

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement1 = connection.prepareStatement(sqlOrder)) {
                preparedStatement1.setInt(1, order.getUniqueReference());
                int rowsAffected = preparedStatement1.executeUpdate();

                if (rowsAffected == 0) {
                    throw new IllegalArgumentException("Order with reference " + order.getUniqueReference() + " already exists");
                }

                try (PreparedStatement statement1 = connection.prepareStatement(sqlOrderStatus)) {
                    statement1.setObject(1, Status.CREATED, Types.OTHER);
                    statement1.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                    statement1.setInt(3, order.getUniqueReference());
                    statement1.executeUpdate();
                }

                dishOrderDao.insertDishOrders(order.getDishOrders(), order.getUniqueReference());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void changeStatusOrder(List<DishOrder> dishOrders, int referenceOrder) {
        List<DishOrderStatus> dishOrderStatusList = new ArrayList<>();
        for (DishOrder dishOrder : dishOrders) {
            dishOrderStatusList.add(dishOrder.getActualStatus());
        }


        boolean isCreated = dishOrderStatusList.stream()
                .allMatch(dishOrderStatus -> dishOrderStatus.getDishOrderStatus().equals(Status.CREATED));
        boolean isConfirmed = dishOrderStatusList.stream()
                .allMatch(dishOrderStatus -> dishOrderStatus.getDishOrderStatus().equals(Status.CONFIRMED));
        boolean isInPreparation = dishOrderStatusList.stream()
                .allMatch(dishOrderStatus -> dishOrderStatus.getDishOrderStatus().equals(Status.IN_PREPARATION));
        boolean isCompleted = dishOrderStatusList.stream()
                .allMatch(dishOrderStatus -> dishOrderStatus.getDishOrderStatus().equals(Status.COMPLETED));
        boolean isServed = dishOrderStatusList.stream()
                .allMatch(dishOrderStatus -> dishOrderStatus.getDishOrderStatus().equals(Status.SERVED));

        if (isCreated) {
            insert(Status.CONFIRMED, referenceOrder);
        } else if (isConfirmed) {
            insert(Status.IN_PREPARATION, referenceOrder);
        } else if (isInPreparation) {
            insert(Status.COMPLETED, referenceOrder);
        } else if (isCompleted) {
            insert(Status.SERVED, referenceOrder);
        } else if (isServed) {
            throw new IllegalArgumentException("Order is Already Served");
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
            preparedStatement.setObject(1, status.name(), Types.OTHER);
            preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setInt(3, referenceOrder);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
