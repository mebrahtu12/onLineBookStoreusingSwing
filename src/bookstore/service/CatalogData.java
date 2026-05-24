package bookstore.service;

import bookstore.model.Book;
import bookstore.model.Category;
import bookstore.patterns.inventory.Inventory;

public final class CatalogData {
    private CatalogData() {
    }

    public static void loadSampleCatalog(Inventory inventory) {
        Category fiction = new Category("Fiction");
        Category sciFi = new Category("Sci-Fi");
        fiction.addComponent(sciFi);

        sciFi.addComponent(book("Dune", 25.00, "Sci-Fi"));
        sciFi.addComponent(book("Foundation", 22.50, "Sci-Fi"));
        sciFi.addComponent(book("Neuromancer", 19.99, "Sci-Fi"));
        sciFi.addComponent(book("The Martian", 16.50, "Sci-Fi"));
        sciFi.addComponent(book("Project Hail Mary", 24.00, "Sci-Fi"));
        sciFi.addComponent(book("Ender's Game", 18.00, "Sci-Fi"));

        Category fantasy = new Category("Fantasy");
        fantasy.addComponent(book("The Hobbit", 18.50, "Fantasy"));
        fantasy.addComponent(book("A Game of Thrones", 22.00, "Fantasy"));
        fantasy.addComponent(book("The Name of the Wind", 20.00, "Fantasy"));
        fantasy.addComponent(book("Mistborn", 17.50, "Fantasy"));
        fantasy.addComponent(book("The Way of Kings", 28.00, "Fantasy"));
        fantasy.addComponent(book("Harry Potter and the Sorcerer's Stone", 15.99, "Fantasy"));
        fiction.addComponent(fantasy);

        Category mystery = new Category("Mystery");
        mystery.addComponent(book("The Girl with the Dragon Tattoo", 14.50, "Mystery"));
        mystery.addComponent(book("Gone Girl", 13.99, "Mystery"));
        mystery.addComponent(book("The Da Vinci Code", 12.50, "Mystery"));
        mystery.addComponent(book("Murder on the Orient Express", 11.00, "Mystery"));
        fiction.addComponent(mystery);

        Category programming = new Category("Programming");
        programming.addComponent(book("Effective Java", 45.00, "Programming"));
        programming.addComponent(book("Clean Code", 38.00, "Programming"));
        programming.addComponent(book("Design Patterns", 42.00, "Programming"));
        programming.addComponent(book("Head First Java", 35.00, "Programming"));
        programming.addComponent(book("Java: The Complete Reference", 40.00, "Programming"));
        programming.addComponent(book("Refactoring", 36.50, "Programming"));
        programming.addComponent(book("The Pragmatic Programmer", 39.00, "Programming"));

        Category history = new Category("History");
        history.addComponent(book("Sapiens", 21.00, "History"));
        history.addComponent(book("Guns, Germs and Steel", 19.50, "History"));
        history.addComponent(book("The Silk Roads", 23.00, "History"));
        history.addComponent(book("1776", 17.00, "History"));
        history.addComponent(book("A People's History of the United States", 20.50, "History"));
        history.addComponent(book("The Wright Brothers", 16.00, "History"));

        Category biography = new Category("Biography");
        biography.addComponent(book("Steve Jobs", 18.00, "Biography"));
        biography.addComponent(book("Becoming", 17.50, "Biography"));
        biography.addComponent(book("Long Walk to Freedom", 16.50, "Biography"));
        biography.addComponent(book("The Diary of a Young Girl", 12.00, "Biography"));

        Category science = new Category("Science");
        science.addComponent(book("The Selfish Gene", 15.00, "Science"));
        science.addComponent(book("A Brief History of Time", 17.00, "Science"));
        science.addComponent(book("Cosmos", 19.00, "Science"));
        science.addComponent(book("The Origin of Species", 14.50, "Science"));
        science.addComponent(book("Silent Spring", 13.50, "Science"));
        science.addComponent(book("The Immortal Life of Henrietta Lacks", 16.00, "Science"));

        inventory.addCategory(fiction);
        inventory.addCategory(programming);
        inventory.addCategory(history);
        inventory.addCategory(biography);
        inventory.addCategory(science);
    }

    private static Book book(String title, double price, String genre) {
        return new Book(title, price, genre, 5);
    }
}
