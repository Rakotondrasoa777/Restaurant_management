package entity;

import java.sql.Timestamp;
import java.util.Objects;

public class Stock {
    private int id_stock;
    private Move move_type;
    private float quantity_ingredient_available;
    private Unit unit;
    private Timestamp date_of_move;
    private int id_ingredient;

    public Stock() {};

    public int getId_stock() {
        return id_stock;
    }

    public void setId_stock(int id_stock) {
        this.id_stock = id_stock;
    }

    public Move getMove_type() {
        return move_type;
    }

    public void setMove_type(Move move_type) {
        this.move_type = move_type;
    }

    public float getQuantity_ingredient_available() {
        return quantity_ingredient_available;
    }

    public void setQuantity_ingredient_available(float quantity_ingredient_available) {
        this.quantity_ingredient_available = quantity_ingredient_available;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Timestamp getDate_of_move() {
        return date_of_move;
    }

    public void setDate_of_move(Timestamp date_of_move) {
        this.date_of_move = date_of_move;
    }

    public int getId_ingredient() {
        return id_ingredient;
    }

    public void setId_ingredient(int id_ingredient) {
        this.id_ingredient = id_ingredient;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id_stock=" + id_stock +
                ", move_type=" + move_type +
                ", quantity_ingredient_available=" + quantity_ingredient_available +
                ", unit=" + unit +
                ", date_of_move=" + date_of_move +
                ", id_ingredient=" + id_ingredient +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return id_stock == stock.id_stock && Float.compare(quantity_ingredient_available, stock.quantity_ingredient_available) == 0 && id_ingredient == stock.id_ingredient && move_type == stock.move_type && unit == stock.unit && Objects.equals(date_of_move, stock.date_of_move);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_stock, move_type, quantity_ingredient_available, unit, date_of_move, id_ingredient);
    }
}
