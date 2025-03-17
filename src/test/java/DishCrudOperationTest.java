import dao.DishCrudOperations;
import dao.IngredientCrudOperation;
import entity.Dish;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DishCrudOperationTest {
    DishCrudOperations dishCrudOperations = new DishCrudOperations();

    @Test
    void testGetAllDish() {
        List<Dish> dishs =dishCrudOperations.getAll(1, 2);
        assertEquals(1, dishs.size());
        assertEquals(getAllDishExpected(), dishs);
    }

    @Test
    void testGetDishByName() {
        Dish dishExpected = new Dish();
        dishExpected.setId_dish(1);
        dishExpected.setName("Hot Dog");
        dishExpected.setUnitPrice(15000);

        Dish dish = dishCrudOperations.findByName("Hot Dog");

        assertEquals(dishExpected, dish);
    }

    @Test
    void testGetIngredientCost() {
        IngredientCrudOperation ingredientCrudOperation = new IngredientCrudOperation();

        Dish dishExpected = new Dish();
        dishExpected.setId_dish(1);
        dishExpected.setName("Hot Dog");
        dishExpected.setUnitPrice(15000);
        dishExpected.setDishIngredientList(ingredientCrudOperation.getIngredientOfDishByID(dishExpected.getId_dish()));

        assertEquals(5500, dishExpected.getIngredientCost());
    }

    @Test
    void testSaveDish() {
        List<Dish> listExpected = getAllDishToSaveExpected();

        List<Dish> actual = dishCrudOperations.saveAll(listExpected);

        assertEquals(listExpected, actual);
    }

    @Test
    void testGetAvailableDish() {
        IngredientCrudOperation operation = new IngredientCrudOperation();
        Dish dish = new Dish();
        dish.setId_dish(1);
        dish.setName("Hot Dog");
        dish.setUnitPrice(15000);
        dish.setDishIngredientList(operation.getIngredientOfDishByID(dish.getId_dish()));

        assertEquals(30, dish.getAvailableDish());
    }

    private List<Dish> getAllDishToSaveExpected() {
        List<Dish> listExpected = new ArrayList<>();
        Dish dish2 = new Dish();
        dish2.setId_dish(1);
        dish2.setName("Hot Dog");
        dish2.setUnitPrice(15000);

        listExpected.add(dish2);

        return listExpected;
    }

    private List<Dish> getAllDishExpected() {
        List<Dish> dishExpected = new ArrayList<>();

        Dish dish = new Dish();
        dish.setId_dish(1);
        dish.setName("Hot Dog");
        dish.setUnitPrice(15000);
        dishExpected.add(dish);

        return dishExpected;
    }
}
