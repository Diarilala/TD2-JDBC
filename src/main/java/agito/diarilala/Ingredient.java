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
    private CategoryEnum category;
    private Dish dish;

    public String getDishName() {
        return dish.getName();
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", category=" + category +
                ", dish=" + getDishName() +
                '}';
    }
}
