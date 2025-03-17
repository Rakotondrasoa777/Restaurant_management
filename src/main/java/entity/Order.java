package entity;

import dao.OrderDao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Order {
    private int uniqueReference;
    private List<OrderStatus> statusOrder;
    private List<DishOrder> listDishOrder;

    public Order() {
    }

    public int getUniqueReference() {
        return uniqueReference;
    }

    public void setUniqueReference(int uniqueReference) {
        this.uniqueReference = uniqueReference;
    }

    public List<OrderStatus> getStatusOrder() {
        return statusOrder;
    }

    public void setStatusOrder(List<OrderStatus> statusOrder) {
        this.statusOrder = statusOrder;
    }

    public void setListDishOrder(List<DishOrder> listDishOrder) {
        this.listDishOrder = listDishOrder;
    }

    public OrderStatus getActualStatus() {
        return statusOrder.stream()
                .max(Comparator.comparing(OrderStatus::getDateOrderStatus))
                .get();
    }

    public List<DishOrder> getDishOrders() {
        return listDishOrder;
    }

    public double getTotalAmount() {
        return listDishOrder.stream()
                .mapToDouble(dishOrder -> dishOrder.getDish().getUnitPrice() * dishOrder.getQuantityOfDish())
                .sum();
    }

    public void changeStatusDishOrderInOrder(int id_dish_order) {
        OrderDao orderDao = new OrderDao();

        for (DishOrder dishOrder : listDishOrder) {
            if (dishOrder.getIdDishOrder() == id_dish_order) {
                dishOrder.changeStatus(id_dish_order);
            } else {
                throw new IllegalArgumentException("L' ID : "+id_dish_order+ " est introuvable");
            }
        }

        orderDao.changeStatusOrder(listDishOrder, id_dish_order);

    }

    @Override
    public String toString() {
        return "Order{" +
                "uniqueReference=" + uniqueReference +
                ", statusOrder=" + statusOrder +
                ", listDishOrder=" + listDishOrder +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return uniqueReference == order.uniqueReference && Objects.equals(statusOrder, order.statusOrder) && Objects.equals(listDishOrder, order.listDishOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueReference, statusOrder, listDishOrder);
    }
}

