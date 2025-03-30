    package entity;

    import dao.DishOrderDao;

    import java.util.ArrayList;
    import java.util.Comparator;
    import java.util.List;
    import java.util.Objects;

    public class DishOrder {
        private int idDishOrder;
        private Dish dish;
        private int quantityOfDish;
        private List<DishOrderStatus> statusDishOrder;
        private int referenceOrder;
        DishOrderDao dao = new DishOrderDao();

        public DishOrder() {
            statusDishOrder = new ArrayList<>();
        }

        public DishOrder(int referenceOrder) {
            this.referenceOrder = referenceOrder;
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
            return dao.getDishOrderStatusByIdInOrder(getIdDishOrder(), getReferenceOrder()).stream()
                    .max(Comparator.comparing(DishOrderStatus::getDateDishOrderStatus))
                    .get();
        }
        public int getReferenceOrder() {
            return referenceOrder;
        }

        public void setReferenceOrder(int referenceOrder) {
            this.referenceOrder = referenceOrder;
        }

        public void changeStatus() {
            DishOrderDao dishOrderDao = new DishOrderDao();
            dishOrderDao.updateDishOrderStatus(idDishOrder, 1);
        }

        @Override
        public String toString() {
            return "DishOrder{" +
                    "idDishOrder=" + idDishOrder +
                    ", dish=" + dish +
                    ", quantityOfDish=" + quantityOfDish +
                    ", statusDishOrder=" + statusDishOrder +
                    ", referenceOrder=" + referenceOrder +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DishOrder dishOrder = (DishOrder) o;
            return idDishOrder == dishOrder.idDishOrder && quantityOfDish == dishOrder.quantityOfDish && referenceOrder == dishOrder.referenceOrder && Objects.equals(dish, dishOrder.dish) && Objects.equals(statusDishOrder, dishOrder.statusDishOrder);
        }

        @Override
        public int hashCode() {
            return Objects.hash(idDishOrder, dish, quantityOfDish, statusDishOrder, referenceOrder);
        }
    }
