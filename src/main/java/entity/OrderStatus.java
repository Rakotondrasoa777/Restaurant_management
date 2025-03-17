package entity;

import java.sql.Timestamp;
import java.util.Objects;

public class OrderStatus {
    private Status orderStatus;
    private Timestamp dateOrderStatus;

    public OrderStatus() {
    }

    public Status getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Status orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Timestamp getDateOrderStatus() {
        return dateOrderStatus;
    }

    public void setDateOrderStatus(Timestamp dateOrderStatus) {
        this.dateOrderStatus = dateOrderStatus;
    }

    @Override
    public String toString() {
        return "OrderStatus{" +
                "orderStatus=" + orderStatus +
                ", dateOrderStatus=" + dateOrderStatus +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderStatus that = (OrderStatus) o;
        return orderStatus == that.orderStatus && Objects.equals(dateOrderStatus, that.dateOrderStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderStatus, dateOrderStatus);
    }
}
