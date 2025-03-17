package entity;

import java.util.Objects;

public class DishIngredient {
    private int id_dish;
    private int id_ingredient;
    private double requiredQuantity;
    private Unit unit;

    public DishIngredient() {}

    public int getId_dish() {
        return id_dish;
    }

    public void setId_dish(int id_dish) {
        this.id_dish = id_dish;
    }

    public int getId_ingredient() {
        return id_ingredient;
    }

    public void setId_ingredient(int id_ingredient) {
        this.id_ingredient = id_ingredient;
    }

    public double getRequiredQuantity() {
        return requiredQuantity;
    }

    public void setRequiredQuantity(double requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "DishIngredient{" +
                "id_dish=" + id_dish +
                ", id_ingredient=" + id_ingredient +
                ", requiredQuantity=" + requiredQuantity +
                ", unit=" + unit +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DishIngredient that = (DishIngredient) o;
        return id_dish == that.id_dish && id_ingredient == that.id_ingredient && Double.compare(requiredQuantity, that.requiredQuantity) == 0 && unit == that.unit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_dish, id_ingredient, requiredQuantity, unit);
    }
}

