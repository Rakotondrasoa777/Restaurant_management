import dao.IngredientCrudOperation;
import dao.StockCrudOperation;
import entity.*;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
class StockCrudOpertionTest {
    StockCrudOperation stockCrudOperation = new StockCrudOperation();

    @Test
    void testGetStockIngredientByMove() {
        List<Stock> stockActual = stockCrudOperation.getStockIngredientByMove(1,1, Move.ENTER);
        assertEquals(1, stockActual.size());
        assertEquals(getStockListExpected(), stockActual);
    }

    @Test
    void testGetIngredientStockStatus() {
        IngredientCrudOperation ingredientCrudOperation = new IngredientCrudOperation();
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId_ingredient(3);
        ingredient1.setName("Oeuf");
        ingredient1.setUnitPrice(ingredientCrudOperation.getIngredientById(ingredient1.getId_ingredient()).getUnitPrice());
        ingredient1.setUnit(Unit.U);
        ingredient1.setUpdateDatetime(Timestamp.valueOf("2025-02-10 12:00:00"));

        StockStatus stockStatusExpected = new StockStatus();
        stockStatusExpected.setQuantity(100);
        stockStatusExpected.setUnit(Unit.U);

        assertEquals(stockStatusExpected, ingredient1.getStockStatus(Timestamp.valueOf("2025-02-01 08:00:00")));
    }

    @Test
    void testGetStockOfIngredient() {
        List<Stock> stockActual = stockCrudOperation.getStockOfIngredient(1,1, 3);
        assertEquals(1, stockActual.size());
        assertEquals(getStockListExpected(), stockActual);
    }

    @Test
    void testGetAvailableQuantity() {
        Ingredient ingredient = new Ingredient();
        ingredient.setId_ingredient(3);
        ingredient.setName("Oeuf");
        ingredient.setUnitPrice(1000);
        ingredient.setUnit(Unit.U);
        ingredient.setUpdateDatetime(Timestamp.valueOf("2025-02-10 11:00:00"));

        double actual = ingredient.getAvailableQuantity(new Timestamp (System.currentTimeMillis()));
        assertEquals(80, actual);
    }

    private List<Stock> getStockListExpected() {
        List<Stock> stockList = new ArrayList<>();
        Stock stock = new Stock();
        stock.setId_stock(1);
        stock.setMove_type(Move.ENTER);
        stock.setQuantity_ingredient_available(100);
        stock.setUnit(Unit.U);
        stock.setDate_of_move(Timestamp.valueOf("2025-02-01 08:00:00"));
        stock.setId_ingredient(3);
        stockList.add(stock);
        return stockList;
    }
}
