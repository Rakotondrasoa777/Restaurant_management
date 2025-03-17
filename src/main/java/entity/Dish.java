package entity;

import dao.DishCrudOperations;
import dao.DishIngredientCrudOperation;
import dao.IngredientCrudOperation;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Dish {
    private int id_dish;
    private String name;
    private int unitPrice;
    private List<Ingredient> ingredientList;

    IngredientCrudOperation ingredientCrudOperation = new IngredientCrudOperation();

    public Dish() {
        this.ingredientList = ingredientCrudOperation.getIngredientOfDishByID(this.id_dish);
    }

    public int getId_dish() {
        return id_dish;
    }

    public void setId_dish(int id_dish) {
        this.id_dish = id_dish;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public void setDishIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public int getIngredientCost() {
        int result = 0;

        DishIngredientCrudOperation operationDishIngredient = new DishIngredientCrudOperation();
        List<IngredientAndRequiredQuantity> resultIngredientAndRequiredQuantity = operationDishIngredient.getIngredientAndRequiredQuantityByIdDish(id_dish);

        for (IngredientAndRequiredQuantity ingredientAndRequiredQuantity : resultIngredientAndRequiredQuantity) {
            if (ingredientAndRequiredQuantity.getDishId() == id_dish) {
                result += (ingredientAndRequiredQuantity.getUnitPrice() * ingredientAndRequiredQuantity.getRequiredQuantity());
            }
        }

        return result;
    }

    public int getIngredientCost(Date datePrice) {
        int result = 0;

        DishIngredientCrudOperation operation = new DishIngredientCrudOperation();
        List<IngredientAndRequiredQuantity> resultIngredientAndRequiredQuantity = operation.getIngredientAndRequiredQuantityByIdDish(id_dish, datePrice);

        for (IngredientAndRequiredQuantity ingredientAndRequiredQuantity : resultIngredientAndRequiredQuantity) {
            if (ingredientAndRequiredQuantity.getDishId() == id_dish) {
                result += (ingredientAndRequiredQuantity.getUnitPrice() * ingredientAndRequiredQuantity.getRequiredQuantity());
            }
        }

        return result;
    }

    private Double getCrossMargin() {
        Double result;

        result = (double) (this.getUnitPrice() - getIngredientCost());

        if (result < 0) {
            return result * -1;
        }
        return result;
    }

    private Double getCrossMargin(Date datePrice) {
        Double result;

        result = (double) (this.getUnitPrice() - getIngredientCost(datePrice));

        if (result < 0) {
            return result * -1;
        }
        return result;
    }

    public int getAvailableDish() {
        DishIngredientCrudOperation operation = new DishIngredientCrudOperation();
        List<Integer> dishAvailable = new ArrayList<>();
        //recuperer l'id des ingredient et les quantite requise pour cette plat
        List<IngredientAndRequiredQuantity> listIngredient= operation.getIngredientAndRequiredQuantityByIdDish(this.id_dish);

        for (IngredientAndRequiredQuantity ingredientAndRequiredQuantity : listIngredient) {
            for (Ingredient ingredient : ingredientList) {
                if (ingredient.getId_ingredient() == ingredientAndRequiredQuantity.getIngredientId()) {
                    dishAvailable.add((int) (ingredient.getAvailableQuantity(new Timestamp(System.currentTimeMillis())) / ingredientAndRequiredQuantity.getRequiredQuantity()));
                }
            }
        }

        return dishAvailable.stream().min(Integer::compareTo).get();

    }

    public static void main(String[] args) {
        IngredientCrudOperation operation = new IngredientCrudOperation();
        Dish dish = new Dish();
        dish.setId_dish(1);
        dish.setName("Hot Dog");
        dish.setUnitPrice(15000);
        dish.setDishIngredientList(operation.getIngredientOfDishByID(dish.getId_dish()));

        System.out.println(dish.getAvailableDish());
    }
    @Override
    public String toString() {
        return "Dish{" +
                "id_dish=" + id_dish +
                ", name='" + name + '\'' +
                ", unitPrice=" + unitPrice +
                ", ingredientList=" + ingredientList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dish dish = (Dish) o;
        return id_dish == dish.id_dish && unitPrice == dish.unitPrice && Objects.equals(name, dish.name) && Objects.equals(ingredientList, dish.ingredientList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_dish, name, unitPrice, ingredientList);
    }
}
