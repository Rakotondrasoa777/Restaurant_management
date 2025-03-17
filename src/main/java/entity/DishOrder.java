package entity;

import dao.DishOrderDao;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class DishOrder {
    private int idDishOrder;
    private Dish dish;
    private int quantityOfDish;
    private List<DishOrderStatus> statusDishOrder;

    public DishOrder() {
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public int getIdDishOrder() {
        return idDishOrder;
    }

    public void setIdDishOrder(int idDishOrder) {
        this.idDishOrder = idDishOrder;
    }

    public int getQuantityOfDish() {
        return quantityOfDish;
    }

    public void setQuantityOfDish(int quantityOfDish) {
        this.quantityOfDish = quantityOfDish;
    }

    public List<DishOrderStatus> getStatusDishOrder() {
        return statusDishOrder;
    }

    public void setStatusDishOrder(List<DishOrderStatus> statusDishOrder) {
        this.statusDishOrder = statusDishOrder;
    }

    public DishOrderStatus getActualStatus() {
        return statusDishOrder.stream()
                .max(Comparator.comparing(DishOrderStatus::getDateDishOrderStatus))
                .get();
    }

    public void changeStatus(int id_dish_order) {
        DishOrderDao dishOrderDao = new DishOrderDao();
        dishOrderDao.updateDishOrderStatus(id_dish_order);
    }

    @Override
    public String toString() {
        return "DishOrder{" +
                "idDishOrder=" + idDishOrder +
                ", dish=" + dish.getName() +
                ", quantityOfDish=" + quantityOfDish +
                ", statusDishOrder=" + statusDishOrder.stream().toList() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DishOrder dishOrder = (DishOrder) o;
        return idDishOrder == dishOrder.idDishOrder && quantityOfDish == dishOrder.quantityOfDish && Objects.equals(dish, dishOrder.dish) && Objects.equals(statusDishOrder, dishOrder.statusDishOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDishOrder, dish, quantityOfDish, statusDishOrder);
    }
}
