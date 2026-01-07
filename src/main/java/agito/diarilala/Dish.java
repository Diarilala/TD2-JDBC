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

    public Double getDishPrice() {
        throw new RuntimeException("Not Implemented");
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
