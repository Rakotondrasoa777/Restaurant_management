package dao;

import db.DataSource;
import entity.Move;
import entity.Stock;
import entity.Unit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StockCrudOperation {
    private final DataSource dataSource = new DataSource();

    public List<Stock> getStockIngredientByMove(int page, int size, Move move) {
        if(page < 0) {
            throw new IllegalArgumentException("page cannot be negative");
        }

        String sql = "select id_stock, move_type, quantity_ingredient_available, unit, date_of_move, id_ingredient from stock" +
                " where move_type::text = ? order by id_stock limit ? offset ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, String.valueOf(Move.ENTER));
            preparedStatement.setInt(2, size);
            preparedStatement.setInt(3, size * (page - 1));

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Stock> stocks = mapGetStockIngredientResultset(resultSet);
                return stocks;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Stock> getStockIngredientByMoveAndById(int page, int size, Move move, int idIngredient) {
        if(page < 0) {
            throw new IllegalArgumentException("page cannot be negative");
        }

        String sql = "select id_stock, move_type, quantity_ingredient_available, unit, date_of_move, id_ingredient from stock" +
                " where move_type::text = ? and id_ingredient = ? order by id_stock limit ? offset ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, String.valueOf(Move.ENTER));
            preparedStatement.setInt(2, idIngredient);
            preparedStatement.setInt(3, size);
            preparedStatement.setInt(4, size * (page - 1));

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Stock> stocks = mapGetStockIngredientResultset(resultSet);
                return stocks;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Stock> getStockOfIngredient(int page, int size, int idIngredient) {
        if(page < 0) {
            throw new IllegalArgumentException("page cannot be negative");
        }

        String sql = "select * from stock where id_ingredient = ? order by id_stock limit ? offset ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idIngredient);
            preparedStatement.setInt(2, size);
            preparedStatement.setInt(3, size * (page - 1));

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Stock> stocks = mapGetStockIngredientResultset(resultSet);
                return stocks;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private List<Stock> mapGetStockIngredientResultset(ResultSet resultSet) throws SQLException {
        List<Stock> stockList = new ArrayList<>();
        while(resultSet.next()) {
            Stock stock = new Stock();
            stock.setId_stock(resultSet.getInt("id_stock"));
            stock.setMove_type(Move.valueOf(resultSet.getString("move_type")));
            stock.setQuantity_ingredient_available(resultSet.getFloat("quantity_ingredient_available"));
            stock.setUnit(Unit.valueOf(resultSet.getString("unit")));
            stock.setDate_of_move(resultSet.getTimestamp("date_of_move"));
            stock.setId_ingredient(resultSet.getInt("id_ingredient"));

            stockList.add(stock);
        }

        return stockList;
    }
}
