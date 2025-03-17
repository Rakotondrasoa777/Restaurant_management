package dao;

import db.DataSource;
import entity.Dish;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DishCrudOperations implements CrudOperations<Dish>{
    private final DataSource dataSource = new DataSource();

    @Override
    public List<Dish> getAll(int page, int size) {
        if (page < 1) {
            throw new IllegalArgumentException("page must be greater than 0 but actual is " + page);
        }

        String sql = "select d.id_dish, d.name, d.unit_price from dish d order by d.id_dish limit ? offset ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setInt(1, size);
                preparedStatement.setInt(2, size * (page - 1));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Dish> dishList = mapDishFromResultSet(resultSet);
                return dishList;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Dish findByName(String name) {
        String sql = "select d.id_dish, d.name, d.unit_price from dish d where d.name=?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, name);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Dish dishName = mapDishByNameFromResultset(resultSet);
                return dishName;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Dish> saveAll(List<Dish> listDish) {
        String upsert = "insert into dish (id_dish, name, unit_price) values (?, ?, ?)" +
                "on conflict (id_dish) do update set name=EXCLUDED.name, unit_price=EXCLUDED.unit_price";

        List<Dish> newDish = new ArrayList<>();
        try(Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            try(PreparedStatement preparedStatement = connection.prepareStatement(upsert)) {
                for (Dish dish : listDish) {
                    preparedStatement.setInt(1,dish.getId_dish());
                    preparedStatement.setString(2, dish.getName());
                    preparedStatement.setInt(3, dish.getUnitPrice());

                    preparedStatement.addBatch();
                }

                preparedStatement.executeBatch();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Erreur lors de la sauvegarde des auteurs : " + e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (Dish dish : listDish) {
            newDish.add(findByName(dish.getName()));
        }

        return newDish;
    }


    private List<Dish> mapDishFromResultSet(ResultSet resultSet) throws SQLException {
        List<Dish> dishs = new ArrayList<>();
        while (resultSet.next()) {
            Dish dish = new Dish();
            dish.setId_dish(resultSet.getInt("id_dish"));
            dish.setName(resultSet.getString("name"));
            dish.setUnitPrice(resultSet.getInt("unit_price"));

            dishs.add(dish);
        }
        return dishs;
    }

    private Dish mapDishByNameFromResultset(ResultSet resultSet) throws SQLException {
        Dish dish = new Dish();
        while (resultSet.next()) {
            dish.setId_dish(resultSet.getInt("id_dish"));
            dish.setName(resultSet.getString("name"));
            dish.setUnitPrice(resultSet.getInt("unit_price"));
        }
        return dish;
    }
}
