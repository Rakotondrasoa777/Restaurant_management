package dao;

import db.DataSource;
import entity.DishIngredient;
import entity.IngredientAndRequiredQuantity;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DishIngredientCrudOperation {
    private final DataSource dataSource = new DataSource();

    public List<IngredientAndRequiredQuantity> getIngredientAndRequiredQuantityByIdDish(int DishId) {
        String sql = "SELECT d.id_dish, di.id_ingredient, hp.ingredient_price, di.required_quantity \n" +
                "FROM dish d\n" +
                "INNER JOIN dish_ingredient di ON d.id_dish = di.id_dish\n" +
                "INNER JOIN ingredient i ON di.id_ingredient = i.id_ingredient\n" +
                "INNER JOIN history_price hp ON i.id_ingredient = hp.id_ingredient\n" +
                "WHERE d.id_dish = ? AND hp.date_price = (\n" +
                "       SELECT MAX(date_price) from history_price where id_ingredient = i.id_ingredient)" +
                " ORDER BY d.id_dish";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, DishId);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                List<IngredientAndRequiredQuantity> ingredientAndRequiredQuantity = mapIngredientAndRequiredQuantity(resultSet);
                return ingredientAndRequiredQuantity;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<IngredientAndRequiredQuantity> getIngredientAndRequiredQuantityByIdDish(int DishId, Date date_price) {
        String sql = "SELECT d.id_dish, di.id_ingredient, hp.ingredient_price, di.required_quantity \n" +
                "FROM dish d\n" +
                "INNER JOIN dish_ingredient di ON d.id_dish = di.id_dish\n" +
                "INNER JOIN ingredient i ON di.id_ingredient = i.id_ingredient\n" +
                "INNER JOIN history_price hp ON i.id_ingredient = hp.id_ingredient\n" +
                "WHERE d.id_dish = ? AND hp.date_price = (\n" +
                "SELECT date_price from history_price where id_ingredient = i.id_ingredient AND date_price <= ? order by date_price desc limit 1)" +
                " ORDER BY d.id_dish";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, DishId);
            preparedStatement.setDate(2, date_price);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                List<IngredientAndRequiredQuantity> ingredientAndRequiredQuantity = mapIngredientAndRequiredQuantity(resultSet);
                return ingredientAndRequiredQuantity;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<IngredientAndRequiredQuantity> mapIngredientAndRequiredQuantity(ResultSet resultSet) throws SQLException {
        List<IngredientAndRequiredQuantity> ingredientAndRequiredQuantityList = new ArrayList<>();
        while(resultSet.next()) {
            IngredientAndRequiredQuantity ingredientAndRequiredQuantity = new IngredientAndRequiredQuantity();
            ingredientAndRequiredQuantity.setDishId(resultSet.getInt("id_dish"));
            ingredientAndRequiredQuantity.setIngredientId(resultSet.getInt("id_ingredient"));
            ingredientAndRequiredQuantity.setUnitPrice(resultSet.getInt("ingredient_price"));
            ingredientAndRequiredQuantity.setRequiredQuantity(resultSet.getDouble("required_quantity"));
            ingredientAndRequiredQuantityList.add(ingredientAndRequiredQuantity);
        }
        return ingredientAndRequiredQuantityList;
    }

    public static void main(String[] args) {
        DishIngredientCrudOperation dishIngredientCrudOperation = new DishIngredientCrudOperation();
        System.out.println(dishIngredientCrudOperation.getIngredientAndRequiredQuantityByIdDish(1));
    }
}
