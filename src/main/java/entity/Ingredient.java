package entity;

import db.DataSource;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Ingredient {
    private int id_ingredient;
    private String name;
    private int unitPrice;
    private Unit unit;
    private Timestamp updateDatetime;

    public Ingredient() {};

    private final DataSource dataSource = new DataSource();


    public int getId_ingredient() {
        return id_ingredient;
    }

    public void setId_ingredient(int id_ingredient) {
        this.id_ingredient = id_ingredient;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUnitPrice() {
        return this.unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Timestamp getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Timestamp updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public StockStatus getStockStatus() {
        String sql = "select quantity_ingredient_available, unit from stock where id_ingredient = ?" +
                " and date_of_move = ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, this.id_ingredient);
            preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                StockStatus stockStatus = new StockStatus();

                while (resultSet.next()){
                    stockStatus.setQuantity(resultSet.getFloat("quantity_ingredient_available"));
                    stockStatus.setUnit(Unit.valueOf(resultSet.getString("unit")));
                }

                return stockStatus;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public StockStatus getStockStatus(Timestamp dateOfMove) {
        String sql = "select quantity_ingredient_available, unit from stock where id_ingredient = ?" +
                " and date_of_move = ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, this.id_ingredient);
            preparedStatement.setTimestamp(2, dateOfMove);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                StockStatus stockStatus = new StockStatus();

                while (resultSet.next()){
                    stockStatus.setQuantity(resultSet.getFloat("quantity_ingredient_available"));
                    stockStatus.setUnit(Unit.valueOf(resultSet.getString("unit")));
                }

                return stockStatus;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Double getAvailableQuantity(Timestamp dateOfMove) {
        Double result = 0.0;

        String sql = "select id_stock, move_type, quantity_ingredient_available ,unit, date_of_move, id_ingredient from stock" +
                " where id_ingredient = ? and date_of_move < ? order by date_of_move asc";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setInt(1, this.id_ingredient);
                preparedStatement.setTimestamp(2, dateOfMove);

                try(ResultSet resultSet = preparedStatement.executeQuery()) {
                    List<Stock> stocks = mapIngredientOfResultset(resultSet);
                    for (Stock stock : stocks) {
                        if (stock.getMove_type() == Move.ENTER) {
                            result += stock.getQuantity_ingredient_available();
                        }
                        if(stock.getMove_type() == Move.EXIT) {
                            result -= stock.getQuantity_ingredient_available();
                        }
                    }
                }
            return result;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Stock> mapIngredientOfResultset (ResultSet resultSet) throws SQLException {
        List<Stock> stocks = new ArrayList<>();

        while (resultSet.next()) {
            Stock stock = new Stock();
            stock.setId_stock(resultSet.getInt("id_stock"));
            stock.setMove_type(Move.valueOf(resultSet.getString("move_type")));
            stock.setQuantity_ingredient_available(resultSet.getInt("quantity_ingredient_available"));
            stock.setUnit(Unit.valueOf(resultSet.getString("unit")));
            stock.setDate_of_move(resultSet.getTimestamp("date_of_move"));
            stock.setId_ingredient(resultSet.getInt("id_ingredient"));
            stocks.add(stock);
        }
        return stocks;
    }


    @Override
    public String toString() {
        return "Ingredient{" +
                "id_ingredient=" + id_ingredient +
                ", name='" + name + '\'' +
                ", unitPrice=" + unitPrice +
                ", unit=" + unit +
                ", updateDatetime=" + updateDatetime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return id_ingredient == that.id_ingredient && unitPrice == that.unitPrice && Objects.equals(name, that.name) && unit == that.unit && Objects.equals(updateDatetime, that.updateDatetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_ingredient, name, unitPrice, unit, updateDatetime);
    }
}
