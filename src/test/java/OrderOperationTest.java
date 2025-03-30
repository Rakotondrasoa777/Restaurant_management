import dao.DishOrderDao;
import dao.IngredientCrudOperation;
import dao.OrderDao;
import entity.*;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
public class OrderOperationTest {
    OrderDao orderDao = new OrderDao();
    DishOrderDao dishOrderDao = new DishOrderDao();

    @Test
    void testGetAllOrderStatusInOrder() {
        Order order = new Order();
        order.setUniqueReference(1);
        order.setStatusOrder(orderDao.getAllOrderStatusInOrder(order.getUniqueReference()));
        List<OrderStatus> ordersStatusExpected = getAllOrderStatusInOrderExpected();
        assertEquals(2, order.getStatusOrder().size());
        assertEquals(ordersStatusExpected, order.getStatusOrder());
    }

    @Test
    void testGetActualStatusOrder() {
        OrderStatus orderStatusExpected = new OrderStatus();
        orderStatusExpected.setOrderStatus(Status.CONFIRMED);
        orderStatusExpected.setDateOrderStatus(Timestamp.valueOf("2025-03-20 14:14:00"));

        Order order = new Order();
        order.setUniqueReference(1);
        order.setStatusOrder(orderDao.getAllOrderStatusInOrder(order.getUniqueReference()));

        assertEquals(orderStatusExpected, order.getActualStatus());
    }

    @Test
    void testGetTotalAmountOfOrder() {
        Order order = new Order();
        order.setUniqueReference(1);
        order.setStatusOrder(orderDao.getAllOrderStatusInOrder(order.getUniqueReference()));
        order.setListDishOrder(dishOrderDao.getAllDishOrderByReferenceOrder(order.getUniqueReference()));

        assertEquals(224000, order.getTotalAmount());
    }

    @Test
    void testGetActualDishOrderStatusInOrderByID() {
        DishOrderStatus dishOrderStatusExpected1 = new DishOrderStatus();
        dishOrderStatusExpected1.setDishOrderStatus(Status.CREATED);
        dishOrderStatusExpected1.setDateDishOrderStatus(Timestamp.valueOf("2025-03-16 13:36:00"));

        DishOrderStatus dishOrderStatusExpected2 = new DishOrderStatus();
        dishOrderStatusExpected2.setDishOrderStatus(Status.CONFIRMED);
        dishOrderStatusExpected2.setDateDishOrderStatus(Timestamp.valueOf("2025-03-16 14:15:00"));

        DishOrderStatus dishOrderStatusExpected3 = new DishOrderStatus();
        dishOrderStatusExpected3.setDishOrderStatus(Status.IN_PREPARATION);
        dishOrderStatusExpected3.setDateDishOrderStatus(Timestamp.valueOf("2025-03-16 14:17:00"));

        Order order = new Order();
        order.setUniqueReference(1);
        order.setStatusOrder(orderDao.getAllOrderStatusInOrder(order.getUniqueReference()));
        order.setListDishOrder(dishOrderDao.getAllDishOrderByReferenceOrder(order.getUniqueReference()));

        assertEquals(dishOrderStatusExpected3, order.getActualStatusOfDishOrderByIdInOrder(1));
        assertEquals(dishOrderStatusExpected2, order.getActualStatusOfDishOrderByIdInOrder(2));
        assertEquals(dishOrderStatusExpected1, order.getActualStatusOfDishOrderByIdInOrder(3));
    }

    @Test
    void testConfirmOrder() {
        Order order = new Order();
        order.setUniqueReference(1);
        order.setStatusOrder(orderDao.getAllOrderStatusInOrder(order.getUniqueReference()));
        order.setListDishOrder(dishOrderDao.getAllDishOrderByReferenceOrder(order.getUniqueReference()));

        try {
            order.confirmOrder();
            assertTrue(true);
        } catch (IllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    void testGetDishOrders() {
        List<DishOrder> dishOrdersExpected = getDishOrdersExpected(1);

        Order order = new Order();
        order.setUniqueReference(1);
        order.setStatusOrder(orderDao.getAllOrderStatusInOrder(order.getUniqueReference()));
        order.setListDishOrder(dishOrderDao.getAllDishOrderByReferenceOrder(order.getUniqueReference()));

        assertEquals(dishOrdersExpected, order.getDishOrders());
    }

    @Test
    void testSave() {
        Order order = new Order();
        order.setUniqueReference(1);
        order.setStatusOrder(new ArrayList<>());
        order.setListDishOrder(new ArrayList<>());

        orderDao.save(order);
    }

    @Test
    void testAddDishOrderInOrder() {
        Order order = new Order();
        order.setUniqueReference(1);
        order.setStatusOrder(orderDao.getAllOrderStatusInOrder(order.getUniqueReference()));
        order.setListDishOrder(dishOrderDao.getAllDishOrderByReferenceOrder(order.getUniqueReference()));

        order.addDishOrderInOrder(dishOrderToAdd(order.getUniqueReference()));

    }

    private List<OrderStatus> getAllOrderStatusInOrderExpected() {
        List<OrderStatus> orderStatusList = new ArrayList<>();
        OrderStatus orderStatus1 = new OrderStatus();
        orderStatus1.setOrderStatus(Status.CREATED);
        orderStatus1.setDateOrderStatus(Timestamp.valueOf("2025-03-16 13:34:00"));

        OrderStatus orderStatus2 = new OrderStatus();
        orderStatus2.setOrderStatus(Status.CONFIRMED);
        orderStatus2.setDateOrderStatus(Timestamp.valueOf("2025-03-20 14:14:00"));
        orderStatusList.add(orderStatus1);
        orderStatusList.add(orderStatus2);
        return orderStatusList;
    }

    private List<DishOrder> getDishOrdersExpected(int referenceOrder) {
        IngredientCrudOperation ingredientCrudOperation = new IngredientCrudOperation();
        Dish dish1 = new Dish();
        dish1.setId_dish(1);
        dish1.setName("Hot Dog");
        dish1.setUnitPrice(15000);
        dish1.setDishIngredientList(ingredientCrudOperation.getIngredientOfDishByID(dish1.getId_dish()));

        Dish dish2 = new Dish();
        dish2.setId_dish(2);
        dish2.setName("Omelette");
        dish2.setUnitPrice(8000);
        dish2.setDishIngredientList(ingredientCrudOperation.getIngredientOfDishByID(dish1.getId_dish()));

        Dish dish3 = new Dish();
        dish3.setId_dish(3);
        dish3.setName("Pain au Saucisse");
        dish3.setUnitPrice(10000);
        dish3.setDishIngredientList(ingredientCrudOperation.getIngredientOfDishByID(dish1.getId_dish()));

        List<DishOrder> dishOrderList = new ArrayList<>();
        DishOrder dishOrder1 = new DishOrder();
        dishOrder1.setIdDishOrder(1);
        dishOrder1.setDish(dish1);
        dishOrder1.setQuantityOfDish(10);
        dishOrder1.setStatusDishOrder(dishOrderDao.getDishOrderStatusById(dishOrder1.getIdDishOrder()));
        dishOrder1.setReferenceOrder(referenceOrder);

        DishOrder dishOrder2 = new DishOrder();
        dishOrder2.setIdDishOrder(2);
        dishOrder2.setDish(dish2);
        dishOrder2.setQuantityOfDish(3);
        dishOrder2.setStatusDishOrder(dishOrderDao.getDishOrderStatusById(dishOrder2.getIdDishOrder()));
        dishOrder2.setReferenceOrder(referenceOrder);

        DishOrder dishOrder3 = new DishOrder();
        dishOrder3.setIdDishOrder(3);
        dishOrder3.setDish(dish3);
        dishOrder3.setQuantityOfDish(5);
        dishOrder3.setStatusDishOrder(dishOrderDao.getDishOrderStatusById(dishOrder3.getIdDishOrder()));
        dishOrder3.setReferenceOrder(referenceOrder);

        dishOrderList.add(dishOrder1);
        dishOrderList.add(dishOrder2);
        dishOrderList.add(dishOrder3);

        return dishOrderList;

    }

    private DishOrder dishOrderToAdd(int referenceOrder) {
        IngredientCrudOperation ingredientCrudOperation = new IngredientCrudOperation();
        Dish dish = new Dish();
        dish.setId_dish(3);
        dish.setName("Pain au Saucisse");
        dish.setUnitPrice(10000);
        dish.setDishIngredientList(ingredientCrudOperation.getIngredientOfDishByID(dish.getId_dish()));

        DishOrder dishOrder = new DishOrder();
        dishOrder.setIdDishOrder(3);
        dishOrder.setReferenceOrder(referenceOrder);
        dishOrder.setQuantityOfDish(5);
        dishOrder.setDish(dish);
        dishOrder.setStatusDishOrder(dishOrderDao.getDishOrderStatusById(dishOrder.getIdDishOrder()));


        return dishOrder;
    }

}
