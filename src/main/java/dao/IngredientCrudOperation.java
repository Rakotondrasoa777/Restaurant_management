package dao;

import db.DataSource;
import entity.Ingredient;
import entity.Unit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IngredientCrudOperation {
    private final DataSource dataSource = new DataSource();


    public List<Ingredient> getAllIngredients(int page, int size) {
        String sql = "select i.id_ingredient, i.name, h.ingredient_price, i.unit, i.update_datetime\n" +
                "from ingredient i inner join history_price h on i.id_ingredient = h.id_ingredient\n" +
                "where h.date_price = (\n" +
                "select Max(date_price) from history_price where id_ingredient = i.id_ingredient) order by i.id_ingredient\n" +
                "limit ? offset ?";

        if (page < 1) {
            throw new IllegalArgumentException("page must be greater than 0 but actual is " + page);
        }

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setInt(1, size);
            preparedStatement.setInt(2, size * (page - 1));

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Ingredient> ingredientResult = mapIngredientOfResultset(resultSet);
                return ingredientResult;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };

    public Ingredient getIngredientById(int id) {
        String sql = "select i.id_ingredient, i.name, h.ingredient_price, i.unit, i.update_datetime from ingredient i inner join history_price h\n" +
                "on i.id_ingredient = h.id_ingredient where i.id_ingredient = ? and h.date_price = (\n" +
                "select Max(date_price) from history_price where id_ingredient = i.id_ingredient)";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                Ingredient ingredient = new Ingredient();

                while (resultSet.next()) {
                    ingredient.setId_ingredient(resultSet.getInt("id_ingredient"));
                    ingredient.setName(resultSet.getString("name"));
                    ingredient.setUnitPrice(resultSet.getInt("ingredient_price"));
                    ingredient.setUnit(Unit.valueOf(resultSet.getString("unit")));
                    ingredient.setUpdateDatetime(resultSet.getTimestamp("update_datetime"));
                }

                return ingredient;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Ingredient> getIngredientByName(int page, int size, String name) {
        String sql = "select i.id_ingredient, i.name, h.ingredient_price, i.unit, i.update_datetime from ingredient i inner join history_price\n" +
                "h on i.id_ingredient = h.id_ingredient where name ILIKE ? AND\n" +
                "h.date_price = (select Max(date_price) from history_price where id_ingredient = i.id_ingredient) order by id_ingredient limit ? offset ?";

        if (page < 1) {
            throw new IllegalArgumentException("page must be greater than 0 but actual is " + page);
        }

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setString(1, "%" +name+ "%");
            preparedStatement.setInt(2, size);
            preparedStatement.setInt(3, size *(page - 1));

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Ingredient> ingredientResult = mapIngredientOfResultset(resultSet);
                return ingredientResult;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };

    public List<Ingredient> getIngredientByUnit(int page, int size, Unit unit) {
        String sql = "select i.id_ingredient, i.name, h.ingredient_price, i.unit, i.update_datetime from ingredient i inner join history_price h\n" +
                "on i.id_ingredient = h.id_ingredient where i.unit::text = ? and h.date_price = (\n" +
                "select Max(date_price) from history_price where id_ingredient = i.id_ingredient) order by id_ingredient limit ? offset ?";

        if (page < 1) {
            throw new IllegalArgumentException("page must be greater than 0 but actual is " + page);
        }

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setString(1, String.valueOf(unit));
            preparedStatement.setInt(2, size);
            preparedStatement.setInt(3, size *(page - 1));

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Ingredient> ingredientResult = mapIngredientOfResultset(resultSet);
                return ingredientResult;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };

    public List<Ingredient> getIngredientByPriceRange(int page, int size, int minPrice, int maxPrice) {
        String sql = "select i.id_ingredient, i.name, h.ingredient_price, i.unit, i.update_datetime from ingredient i inner join history_price h\n" +
                "on i.id_ingredient = h.id_ingredient where h.date_price = (select Max(date_price) from history_price where id_ingredient = i.id_ingredient)\n" +
                "AND h.ingredient_price between ? and ? order by i.id_ingredient limit ? offset ?";

        if (page < 1) {
            throw new IllegalArgumentException("page must be greater than 0 but actual is " + page);
        }

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setInt(1, minPrice);
            preparedStatement.setInt(2, maxPrice);
            preparedStatement.setInt(3, size);
            preparedStatement.setInt(4, size *(page - 1));

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Ingredient> ingredientResult = mapIngredientOfResultset(resultSet);
                return ingredientResult;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };

    public List<Ingredient> getIngredientByDateRange(int page, int size, Timestamp dateMin, Timestamp dateMax) {
        String sql = "select i.id_ingredient, i.name, h.ingredient_price, i.unit, i.update_datetime from ingredient i inner join history_price h\n" +
                "on i.id_ingredient = h.id_ingredient where h.date_price = (select Max(date_price) from history_price where id_ingredient = i.id_ingredient)\n" +
                "AND i.update_datetime between ? and ? order by id_ingredient limit ? offset ?";

        if (page < 1) {
            throw new IllegalArgumentException("page must be greater than 0 but actual is " + page);
        }

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setTimestamp(1, dateMin);
            preparedStatement.setTimestamp(2, dateMax);
            preparedStatement.setInt(3, size);
            preparedStatement.setInt(4, size *(page - 1));

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Ingredient> ingredientResult = mapIngredientOfResultset(resultSet);
                return ingredientResult;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };

    public List<Ingredient> getIngredientOfDishByID(int idDish) {
        String sql = "SELECT i.id_ingredient, i.name, hp.ingredient_price, i.unit, i.update_datetime FROM\n" +
                "    ingredient i INNER JOIN dish_ingredient di ON i.id_ingredient = di.id_ingredient\n" +
                "    INNER JOIN history_price hp ON i.id_ingredient = hp.id_ingredient WHERE di.id_dish = ?\n" +
                "    AND hp.date_price = (\n" +
                "        SELECT MAX(date_price)\n" +
                "        FROM history_price\n" +
                "        WHERE id_ingredient = i.id_ingredient\n" +
                "    )\n" +
                "    ORDER BY i.id_ingredient;";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, idDish);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Ingredient> ingredientResult = mapIngredientOfResultset(resultSet);
                return ingredientResult;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Ingredient> mapIngredientOfResultset (ResultSet resultSet) throws SQLException {
        List<Ingredient> ingredients = new ArrayList<>();

        while (resultSet.next()) {
            Ingredient ingredient = new Ingredient();
            ingredient.setId_ingredient(resultSet.getInt("id_ingredient"));
            ingredient.setName(resultSet.getString("name"));
            ingredient.setUnitPrice(resultSet.getInt("ingredient_price"));
            ingredient.setUnit(Unit.valueOf(resultSet.getString("unit")));
            ingredient.setUpdateDatetime(resultSet.getTimestamp("update_datetime"));
            ingredients.add(ingredient);
        }
        return ingredients;
    }

}
