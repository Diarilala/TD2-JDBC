CREATE TYPE type AS ENUM('START','MAIN','DESSERT');
CREATE TYPE category AS ENUM('VEGETABLE','ANIMAL','MARINE','DAIRY','OTHER');

CREATE TABLE Dish(
    id int PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    dish_type type NOT NULL,
);

CREATE TABLE Ingredient(
    id int PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    price NUMERIC(10,2) NOT NULL,
    category category NOT NULL,
    id_dish int REFERENCES Dish(id);
)