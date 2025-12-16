package agito.diarilala;

import jdk.jfr.Category;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Ingredient {
    private int id;
    private String name;
    private double price;
    private Category category;
    private Dish dish;

    public String getDishName() {
        throw new RuntimeException("Not implemented");
    }
}
