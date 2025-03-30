package entity;

import dao.DishOrderDao;
import dao.OrderDao;

import java.util.*;

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
                .max(Comparator.comparing(OrderStatus::getDateOrderStatus)).get();
    }

    public List<DishOrder> getDishOrders() {
        return listDishOrder;
    }

    public double getTotalAmount() {
        return listDishOrder.stream()
                .mapToDouble(dishOrder -> dishOrder.getDish().getUnitPrice() * dishOrder.getQuantityOfDish())
                .sum();
    }

    public DishOrderStatus getActualStatusOfDishOrderByIdInOrder(int dishOrderId) {
        for (DishOrder dishOrder : listDishOrder) {
            if (dishOrder.getIdDishOrder() == dishOrderId) {
                return dishOrder.getActualStatus();
            }
        }
        throw new IllegalArgumentException("ID introuvable");
    }

    public void addDishOrderInOrder(DishOrder dishOrder) {

        OrderDao orderDao = new OrderDao();
        if (getActualStatus().getOrderStatus() == Status.CREATED) {
            orderDao.addDishOrderInOrderByIdDish(dishOrder);
        } else {
            throw new IllegalArgumentException("Order is already confirmed and can't be added");
        }
    }

    public void updateDishOrderInOrder(int idDishOrder, int idDish, int quantityDish) {
        DishOrderDao dishOrderDao = new DishOrderDao();
        if (getActualStatus().getOrderStatus() == Status.CREATED) {
            dishOrderDao.updateDishOrder(idDishOrder, idDish, quantityDish);
        } else {
            throw new IllegalArgumentException("Order is already passed");
        }
    }

    public void deleteDishOrderInOrder(int idDishOrder) {
        DishOrderDao dishOrderDao = new DishOrderDao();
        if (getActualStatus().getOrderStatus() == Status.CREATED) {
            dishOrderDao.deleteDishOrder(idDishOrder);
        } else {
            throw new IllegalArgumentException("Order is already passed");
        }
    }

    public void changeStatusDishOrderInOrder(int id) {
        DishOrderDao dishOrderDao = new DishOrderDao();
        OrderDao orderDao = new OrderDao();
        List<DishOrder> dishOrderList = dishOrderDao.getAllDishOrderByReferenceOrder(getUniqueReference());

        Optional<DishOrder> dishOrder = dishOrderList.stream()
                        .filter(d -> d.getIdDishOrder() == id)
                        .findFirst();
        if (dishOrder.isPresent()) {
            dishOrderDao.updateDishOrderStatus(id, getUniqueReference());
            orderDao.changeStatusOrder(dishOrderList, getUniqueReference());
        }
    }

    public void confirmOrder() {
        if (getActualStatus().getOrderStatus() == Status.CONFIRMED) {
            throw new IllegalArgumentException("Order is already confirmed");
        }

        DishOrderDao dishOrderDao = new DishOrderDao();
        for (DishOrder dishOrder : listDishOrder) {
            dishOrderDao.updateDishOrderStatus(dishOrder.getIdDishOrder(), getUniqueReference());
        }
        OrderDao orderDao = new OrderDao();
        orderDao.changeStatusOrder(listDishOrder, getUniqueReference());
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

