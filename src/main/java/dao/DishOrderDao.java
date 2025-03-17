package dao;

import db.DataSource;
import entity.Dish;
import entity.DishOrder;
import entity.DishOrderStatus;
import entity.Status;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DishOrderDao{
    private final DataSource dataSource = new DataSource();

    public List<DishOrder> getAllDishOrderByReferenceOrder(int referenceOrder) {
        String sql1 = "select dio.id_dish_order, d.id_dish, d.name, d.unit_price, dio.quantity_of_dish from\n" +
                "dish d inner join dish_order dio on d.id_dish = dio.id_dish\n" +
                "where dio.reference_order = ?";

        String sql2 = "select dos.status, dos.date_dish_order_status from dish_order_status dos inner join dish_order dio\n" +
                "on dos.id_dish_order = dio.id_dish_order";

        try(Connection connection = dataSource.getConnection()) {
            try(PreparedStatement preparedStatement1 = connection.prepareStatement(sql1)) {
                preparedStatement1.setInt(1, referenceOrder);
                ResultSet resultSet1 = preparedStatement1.executeQuery();

                try(PreparedStatement preparedStatement2  = connection.prepareStatement(sql2)) {
                    ResultSet resultSet2 = preparedStatement2.executeQuery();
                    return mapDishOrderFromResultset(resultSet1, resultSet2);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateDishOrderStatus(int id_dish_order) {
        String sql = "select dos.status, dos.date_dish_order_status from dish_order_status dos inner join dish_order dio" +
                " on dos.id_dish_order = dio.id_dish_order where dos.date_dish_order_status = (" +
                "select Max(date_dish_order_status) from dish_order_status) and dio.id_dish_order = ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, id_dish_order);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                List<DishOrderStatus> dishOrderStatuses = mapDishOrderStatusResultset(resultSet);

                for (DishOrderStatus dishOrderStatus : dishOrderStatuses) {
                    switch (dishOrderStatus.getDishOrderStatus()) {
                        case Status.CREATED:
                            updateStatus(Status.CONFIRMED, id_dish_order);
                            break;
                        case Status.CONFIRMED:
                            updateStatus(Status.IN_PREPARATION, id_dish_order);
                            break;
                        case Status.IN_PREPARATION:
                            updateStatus(Status.COMPLETED, id_dish_order);
                            break;
                        case Status.COMPLETED:
                            updateStatus(Status.SERVED, id_dish_order);
                        default:
                            throw new IllegalArgumentException("Dish is already SERVED");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<DishOrderStatus> getDishOrderStatusById(int id_dish_order) {
        String sql = "select dos.status, dos.date_dish_order_status from dish_order_status dos " +
                "inner join dish_order dio " +
                "on dos.id_dish_order = dio.id_dish_order" +
                " where dos.id_dish_order = ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id_dish_order);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                return mapDishOrderStatusResultset(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public DishOrder findByName(String name) {
        return null;
    }

    private void updateStatus(Status status, int id_dish_order) {
        String sql = "insert into dish_order_status values (?,?,?)";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setObject(1, status.name(), java.sql.Types.OTHER);
            preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setInt(3, id_dish_order);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<DishOrderStatus> mapDishOrderStatusResultset(ResultSet resultSet) throws SQLException {
        List<DishOrderStatus> dishOrderStatusList = new ArrayList<>();
        while (resultSet.next()) {
            DishOrderStatus dishOrderStatus = new DishOrderStatus();
            dishOrderStatus.setDishOrderStatus(Status.valueOf(resultSet.getString("status")));
            dishOrderStatus.setDateDishOrderStatus(resultSet.getTimestamp("date_dish_order_status"));
            dishOrderStatusList.add(dishOrderStatus);
        }
        return dishOrderStatusList;
    }

    private List<DishOrder> mapDishOrderFromResultset (ResultSet resultSet1, ResultSet resultSet2) throws SQLException {
        IngredientCrudOperation ingredientCrudOperation = new IngredientCrudOperation();
        List<DishOrder> dishOrders = new ArrayList<>();
        List<DishOrderStatus> dishOrderStatuses = new ArrayList<>();
        DishOrder dishOrder = new DishOrder();
        Dish dish = new Dish();

        while (resultSet1.next()) {
            dishOrder.setIdDishOrder(resultSet1.getInt("id_dish_order"));
            dish.setId_dish(resultSet1.getInt("id_dish"));
            dish.setName(resultSet1.getString("name"));
            dish.setUnitPrice(resultSet1.getInt("unit_price"));
            dishOrder.setQuantityOfDish(resultSet1.getInt("quantity_of_dish"));
        }
        dish.setDishIngredientList(ingredientCrudOperation.getIngredientOfDishByID(dish.getId_dish()));
        dishOrder.setDish(dish);

        while (resultSet2.next()) {
            DishOrderStatus dishOrderStatus = new DishOrderStatus();
            dishOrderStatus.setDishOrderStatus(Status.valueOf(resultSet2.getString("status")));
            dishOrderStatus.setDateDishOrderStatus(resultSet2.getTimestamp("date_dish_order_status"));
            dishOrderStatuses.add(dishOrderStatus);
        }

        dishOrder.setStatusDishOrder(dishOrderStatuses);

        dishOrders.add(dishOrder);

        return dishOrders;
    }
}
