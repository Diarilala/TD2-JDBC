package agito.diarilala;

import lombok.*;

import java.util.List;

@Data
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dish {
    private int id;
    private String name;
    private DishTypeEnum dishType;
    private List<Ingredient> ingredients;

    public Double getDishCost() {
        double dishCost = 0.0;
        for (Ingredient ingredient : ingredients) {
            dishCost += ingredient.getPrice();
        }
        return dishCost;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dishType=" + dishType +
                ", ingredients=" + ingredients +
                '}';
    }
}
