import dao.IngredientCrudOperation;
import entity.Ingredient;
import entity.Unit;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

class IngredientCrudOperationTest {
    IngredientCrudOperation ingredientCrudOperation = new IngredientCrudOperation();

    @Test
    void testGetAllIngredient() {
        List<Ingredient> dishResultActual = ingredientCrudOperation.getAllIngredients(1, 1);
        assertEquals(dishResultActual.size(), 1);
        assertEquals(resultGetAllIngredientExpected(), dishResultActual);
    }

    @Test
    void testGetIngredientById() {
        Ingredient ingredientActual = ingredientCrudOperation.getIngredientById(1);
        assertEquals(resultGetIngredientByIdExpected(), ingredientActual);
    }

    @Test
    void testGetingredientByName() {
        List<Ingredient> ingredientsActual = ingredientCrudOperation.getIngredientByName(1,1, "Sau");
        assertEquals(ingredientsActual.size(), 1);
        assertEquals(resultGetIngredientByOtherCritereExpected(), ingredientsActual);
    }

    @Test
    void testGetIngredientByUnit() {
        List<Ingredient> ingredientsActual = ingredientCrudOperation.getIngredientByUnit(1,1, Unit.G);
        assertEquals(resultGetIngredientByOtherCritereExpected(), ingredientsActual);
    }

    @Test
    void testGetIngredientByPriceRange() {
        List<Ingredient> ingredientsActual = ingredientCrudOperation.getIngredientByPriceRange(1,1,0,1000);
        assertEquals(ingredientsActual.size(), 1);
        assertEquals(resultGetIngredientByOtherCritereExpected(), ingredientsActual);
    }

    @Test
    void testGetIngredientByDateRange() {
        List<Ingredient> ingredientsActual = ingredientCrudOperation.getIngredientByDateRange(1,1, Timestamp.valueOf("2024-12-20 10:00:00"), Timestamp.valueOf("2025-02-20 10:30:00"));
        assertEquals(ingredientsActual.size(), 1);
        assertEquals(resultGetIngredientByOtherCritereExpected(), ingredientsActual);
    }

    private List<Ingredient> resultGetAllIngredientExpected () {
        List<Ingredient> ingredients = new ArrayList<>();

        Ingredient ingredient = new Ingredient();
        ingredient.setId_ingredient(1);
        ingredient.setName("Saucisse");
        ingredient.setUnitPrice(20);
        ingredient.setUnit(Unit.G);
        ingredient.setUpdateDatetime(Timestamp.valueOf("2025-02-20 10:00:00"));

        ingredients.add(ingredient);
        return ingredients;
    }

    private Ingredient resultGetIngredientByIdExpected () {
        Ingredient ingredient = new Ingredient();
        ingredient.setId_ingredient(1);
        ingredient.setName("Saucisse");
        ingredient.setUnitPrice(20);
        ingredient.setUnit(Unit.G);
        ingredient.setUpdateDatetime(Timestamp.valueOf("2025-02-20 10:00:00"));

        return ingredient;
    }

    private List<Ingredient> resultGetIngredientByOtherCritereExpected () {
        List<Ingredient> ingredients = new ArrayList<>();
        Ingredient ingredient = new Ingredient();
        ingredient.setId_ingredient(1);
        ingredient.setName("Saucisse");
        ingredient.setUnitPrice(20);
        ingredient.setUnit(Unit.G);
        ingredient.setUpdateDatetime(Timestamp.valueOf("2025-02-20 10:00:00"));
        ingredients.add(ingredient);

        return ingredients;
    }

}
