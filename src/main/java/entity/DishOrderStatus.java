package entity;

import java.sql.Timestamp;
import java.util.Objects;

public class DishOrderStatus {
    private Status dishOrderStatus;
    private Timestamp dateDishOrderStatus;

    public DishOrderStatus() {
    }

    public Status getDishOrderStatus() {
        return dishOrderStatus;
    }

    public void setDishOrderStatus(Status dishOrderStatus) {
        this.dishOrderStatus = dishOrderStatus;
    }

    public Timestamp getDateDishOrderStatus() {
        return dateDishOrderStatus;
    }

    public void setDateDishOrderStatus(Timestamp dateDishOrderStatus) {
        this.dateDishOrderStatus = dateDishOrderStatus;
    }

    @Override
    public String toString() {
        return "{" +
                "dishOrderStatus=" + dishOrderStatus +
                ", dateDishOrderStatus=" + dateDishOrderStatus +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DishOrderStatus that = (DishOrderStatus) o;
        return dishOrderStatus == that.dishOrderStatus && Objects.equals(dateDishOrderStatus, that.dateDishOrderStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dishOrderStatus, dateDishOrderStatus);
    }
}
