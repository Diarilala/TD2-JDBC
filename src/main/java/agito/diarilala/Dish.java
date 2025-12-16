package agito.diarilala;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Dish {
    private int id;
    private String name;
    private DishTypeEnum dishType;
    private List<Ingredient> ingredients;

    public Double getDishPrice() {
        return null;
    }
}
