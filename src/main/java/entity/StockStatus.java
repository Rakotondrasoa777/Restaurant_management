package entity;

import java.util.Objects;

public class StockStatus {
    private float quantity;
    private Unit unit;

    public StockStatus() {};

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "StockStatus{" +
                "quantity=" + quantity +
                ", unit=" + unit +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockStatus that = (StockStatus) o;
        return Float.compare(quantity, that.quantity) == 0 && unit == that.unit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity, unit);
    }
}
