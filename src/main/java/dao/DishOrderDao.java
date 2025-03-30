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

        String sql2 = "select dos.id_dish_order, dos.status, dos.date_dish_order_status from dish_order_status dos inner join dish_order dio\n" +
                "on dos.id_dish_order = dio.id_dish_order where dio.reference_order = ?";

        try(Connection connection = dataSource.getConnection()) {
            try(PreparedStatement preparedStatement1 = connection.prepareStatement(sql1)) {
                preparedStatement1.setInt(1, referenceOrder);
                ResultSet resultSet1 = preparedStatement1.executeQuery();

                try(PreparedStatement preparedStatement2  = connection.prepareStatement(sql2)) {
                    preparedStatement2.setInt(1, referenceOrder);
                    ResultSet resultSet2 = preparedStatement2.executeQuery();
                    return mapDishOrderFromResultset(resultSet1, resultSet2);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateDishOrder(int idDishOrder, int idDish, int quantityOfDish) {
        DishCrudOperations dishCrudOperations = new DishCrudOperations();
        IngredientCrudOperation ingredientCrudOperation = new IngredientCrudOperation();
        Dish dish = dishCrudOperations.findDishById(idDish);
        dish.setDishIngredientList(ingredientCrudOperation.getIngredientOfDishByID(dish.getId_dish()));
        int availableDish = dish.getAvailableDish();
        if(availableDish < quantityOfDish) {
            throw new IllegalArgumentException("We have just : "+availableDish+" "+dish.getName());
        }
        String sql = "update dish_order set id_dish = ? ,quantity_of_dish = ? where id_dish_order = ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idDish);
            preparedStatement.setInt(2, quantityOfDish);
            preparedStatement.setInt(3, idDishOrder);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteDishOrder(int idDishOrder) {
        String sql = "delete from dish_order where id_dish_order = ?";
        String sqlStatus = "delete from dish_order_status where id_dish_order = ?";
        try(Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement1 = connection.prepareStatement(sqlStatus)){
                preparedStatement1.setInt(1, idDishOrder);
                preparedStatement1.executeUpdate();
            }

            try(PreparedStatement preparedStatement2 = connection.prepareStatement(sql)) {
                preparedStatement2.setInt(1, idDishOrder);
                preparedStatement2.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateDishOrderStatus(int idDishOrder, int referenceOrder) {
        DishOrderDao dao = new DishOrderDao();
        List<DishOrder> dishOrderList = dao.getAllDishOrderByReferenceOrder(referenceOrder);

        for (DishOrder dishOrder : dishOrderList) {
            switch (dishOrder.getActualStatus().getDishOrderStatus()) {
                case Status.CREATED:
                    updateStatus(Status.CONFIRMED, idDishOrder, referenceOrder);
                    break;
                case Status.CONFIRMED:
                    updateStatus(Status.IN_PREPARATION, idDishOrder, referenceOrder);
                    break;
                case Status.IN_PREPARATION:
                    updateStatus(Status.COMPLETED, idDishOrder, referenceOrder);
                    break;
                case Status.COMPLETED:
                    updateStatus(Status.SERVED, idDishOrder, referenceOrder);
                default:
                    throw new IllegalArgumentException("Dish is already SERVED");
            }
        }
    }

    public List<DishOrderStatus> getDishOrderStatusByIdInOrder(int id_dish_order, int referenceOrder) {
        String sql = "select dos.status, dos.date_dish_order_status from dish_order_status dos " +
                "inner join dish_order dio " +
                "on dos.id_dish_order = dio.id_dish_order" +
                " where dos.id_dish_order = ? and dos.reference_order = ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id_dish_order);
            preparedStatement.setInt(2, referenceOrder);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                return mapDishOrderStatusResultset(resultSet);
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

    public void insertDishOrders(List<DishOrder> dishOrderList, int referenceOrder) {
        String insertDishOrderSql = "insert into dish_order (id_dish_order, id_dish, quantity_of_dish, reference_order) values (?,?,?,?)";
        String insertDishOrderStatusSql = "insert into dish_order_status values (?,?,?,?)";
        try(Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try {
                for (DishOrder dishOrder : dishOrderList) {
                    try(PreparedStatement preparedStatement1 = connection.prepareStatement(insertDishOrderSql)) {
                        preparedStatement1.setInt(1, dishOrder.getIdDishOrder());
                        preparedStatement1.setInt(2, dishOrder.getDish().getId_dish());
                        preparedStatement1.setInt(3, dishOrder.getQuantityOfDish());
                        preparedStatement1.setInt(4, referenceOrder);
                        preparedStatement1.executeUpdate();
                    }
                    try(PreparedStatement preparedStatement2 = connection.prepareStatement(insertDishOrderStatusSql)) {
                        preparedStatement2.setObject(1, Status.CREATED, Types.OTHER);
                        preparedStatement2.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                        preparedStatement2.setInt(3, dishOrder.getIdDishOrder());
                        preparedStatement2.setInt(4, dishOrder.getReferenceOrder());
                        preparedStatement2.executeUpdate();
                    }
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public DishOrder findByName(String name) {
        return null;
    }

    private void updateStatus(Status status, int id_dish_order, int referenceOrder) {
        String sql = "insert into dish_order_status values (?,?,?,?)";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setObject(1, status.name(), java.sql.Types.OTHER);
            preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setInt(3, id_dish_order);
            preparedStatement.setInt(4, referenceOrder);

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
        List<DishOrder> dishOrders = new ArrayList<>();
        IngredientCrudOperation ingredientCrudOperation = new IngredientCrudOperation();

        while (resultSet1.next()) {
            DishOrder dishOrder = new DishOrder();

            dishOrder.setIdDishOrder(resultSet1.getInt("id_dish_order"));
            Dish dish = new Dish();
            dish.setId_dish(resultSet1.getInt("id_dish"));
            dish.setName(resultSet1.getString("name"));
            dish.setUnitPrice(resultSet1.getInt("unit_price"));
            dish.setDishIngredientList(ingredientCrudOperation.getIngredientOfDishByID(dish.getId_dish()));

            dishOrder.setDish(dish);
            dishOrder.setQuantityOfDish(resultSet1.getInt("quantity_of_dish"));

            dishOrders.add(dishOrder);
        }

        while (resultSet2.next()) {
            DishOrderStatus dishOrderStatus = new DishOrderStatus();
            int dishOrderId = resultSet2.getInt("id_dish_order");
            for (DishOrder dishOrder : dishOrders) {
                if (dishOrderId == dishOrder.getIdDishOrder()) {
                    dishOrderStatus.setDishOrderStatus(Status.valueOf(resultSet2.getString("status")));
                    dishOrderStatus.setDateDishOrderStatus(resultSet2.getTimestamp("date_dish_order_status"));
                    dishOrder.getStatusDishOrder().add(dishOrderStatus);
                }
            }
        }
        return dishOrders;
    }
}
