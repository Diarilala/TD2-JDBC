package agito.diarilala;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor

public class DataRetriever {
    private final DBConnection dbConnection;

    Optional<Dish> findDishById(int id) {
        String query = "SELECT d.id AS dish_id, d.name AS dish_name, d.dish_type,i.id AS ingredient_id, i.name AS ingredient_name, " +
                "i.price, i.category FROM Dish d LEFT JOIN " +
                "Ingredient i ON d.id = i.id_dish WHERE d.id = ? ORDER BY i.id ASC;";

        List<Ingredient> ingredients = new ArrayList<>();
        try{Connection connection = dbConnection.getDBConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()){
                return Optional.empty();
            }
            Dish dish = new Dish();

            int dish_id = resultSet.getInt("dish_id");
            String dish_name = resultSet.getString("dish_name");
            String dish_type = resultSet.getString("dish_type");

            dish.setId(dish_id);
            dish.setName(dish_name);
            if(dish_type != null){
                dish.setDishType(DishTypeEnum.valueOf(dish_type));
            }
            do {
                String ingredient_name = resultSet.getString("ingredient_name");
                if(ingredient_name != null){
                    Ingredient ingredient = new Ingredient();
                    ingredient.setId(resultSet.getInt("ingredient_id"));
                    ingredient.setName(ingredient_name);
                    ingredient.setPrice(resultSet.getDouble("price"));
                    String category = resultSet.getString("category");
                    if(category != null){
                        ingredient.setCategory(CategoryEnum.valueOf(category));
                    }
                    ingredients.add(ingredient);
                }
            } while (resultSet.next());
            dish.setIngredients(ingredients);
            return Optional.of(dish);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };

    List<Ingredient> findIngredients(int page, int size) {
        List<Ingredient> ingredients = new ArrayList<>();
        try {
            Connection connection = dbConnection.getDBConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT i.int, i.name, i.price, i.category FROM Ingredient i limit ? offset ?;"
            );
            preparedStatement.setInt(1, size);
            preparedStatement.setInt(2, (page - 1) * size);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String category = resultSet.getString("category");
                Ingredient ingredient = new Ingredient();
                ingredient.setId(resultSet.getInt("ingredient_id"));
                ingredient.setName(resultSet.getString("ingredient_name"));
                ingredient.setPrice(resultSet.getDouble("price"));
                ingredient.setCategory(category == null ? null : CategoryEnum.valueOf(category));
                ingredients.add(ingredient);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ingredients;
    };

    List<Ingredient> createIngredients(List<Ingredient> newIngredients) {
        throw new UnsupportedOperationException("Not supported yet.");
    };

    Dish saveDish(Dish dishToSave){
        throw new UnsupportedOperationException("Not supported yet.");
    };

    List<Dish> findDishesByIngredientName(String ingredientName){
        List<Dish> dishes = new ArrayList<>();
        try {
            Connection connection = dbConnection.getDBConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT DISTINCT d.id AS dish_id, d.name AS dish_name " +
                    "FROM Dish d JOIN Ingredient i ON d.id = i.id_dish " +
                    "WHERE i.name ILIKE ? ORDER BY d.name ASC;");
            preparedStatement.setString(1, "%"+ingredientName+"%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Dish dish = new Dish();
                dish.setId(resultSet.getInt("dish_id"));
                dish.setName(resultSet.getString("dish_name"));
                dishes.add(dish);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dishes;
    };

    List<Ingredient> findIngredientsByCriteria(String ingredientName,
                                               CategoryEnum category,
                                               String dishName,
                                               int page, int size){
        List<Ingredient> ingredients = new ArrayList<>();
        try {
            Connection connection = dbConnection.getDBConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT i.id AS ingredient_id, i.name AS ingredient_name, " +
                            "i.price, i.category, d.id AS dish_id, " +
                            "d.name AS dish_name FROM Ingredient i JOIN Dish d " +
                            "ON d.id = i.dish_id WHERE " +
                            "(? IS NULL OR i.name ILIKE ?) " +
                            "AND (? IS NULL OR i.category = ?) " +
                            "AND (? IS NULL OR d.name ILIKE ?) " +
                            "ORDER BY i.name ASC " +
                            "LIMIT ? OFFSET ?"
            );
            int index = 1;
            if(ingredientName != null){
                preparedStatement.setString(index++, String.valueOf(Types.VARCHAR));
                preparedStatement.setString(index++, String.valueOf(Types.VARCHAR));
            } else{
                preparedStatement.setString(index++, ingredientName);
                preparedStatement.setString(index++, "%"+ingredientName+"%");
            };
            if(category != null){
                preparedStatement.setString(index++, String.valueOf(Types.VARCHAR));
                preparedStatement.setString(index++, String.valueOf(Types.VARCHAR));
            } else {
                preparedStatement.setString(index++, category.name());
                preparedStatement.setString(index++, category.name());
            };
            if(dishName != null){
                preparedStatement.setString(index++, String.valueOf(Types.VARCHAR));
                preparedStatement.setString(index++, String.valueOf(Types.VARCHAR));
            } else {
                preparedStatement.setString(index++, dishName);
                preparedStatement.setString(index++, "%"+dishName+"%");
            };
            preparedStatement.setInt(index++, size);
            preparedStatement.setInt(index, (page - 1)*size);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setId(resultSet.getInt("ingredient_id"));
                    ingredient.setName(resultSet.getString("ingredient_name"));
                    ingredient.setPrice(resultSet.getDouble("price"));
                    ingredient.setCategory(CategoryEnum.valueOf(category.name()));
                    Dish dish = new Dish();
                    dish.setId(resultSet.getInt("dish_id"));
                    dish.setName(resultSet.getString("dish_name"));
                    ingredients.add(ingredient);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ingredients;
    };
}
