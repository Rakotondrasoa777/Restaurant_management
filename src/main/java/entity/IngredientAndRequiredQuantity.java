package entity;

import java.util.Objects;

public class IngredientAndRequiredQuantity {
    private int dishId;
    private int ingredientId;
    private int unitPrice;
    private double requiredQuantity;

    public IngredientAndRequiredQuantity() {}

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getRequiredQuantity() {
        return requiredQuantity;
    }

    public void setRequiredQuantity(double requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }



    @Override
    public String toString() {
        return "IngredientAndRequiredQuantity{" +
                "dishId=" + dishId +
                ", ingredientId=" + ingredientId +
                ", unitPrice=" + unitPrice +
                ", requiredQuantity=" + requiredQuantity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IngredientAndRequiredQuantity that = (IngredientAndRequiredQuantity) o;
        return dishId == that.dishId && ingredientId == that.ingredientId && unitPrice == that.unitPrice && Double.compare(requiredQuantity, that.requiredQuantity) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dishId, ingredientId, unitPrice, requiredQuantity);
    }
}
