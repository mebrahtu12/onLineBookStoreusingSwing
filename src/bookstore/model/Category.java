package bookstore.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Category implements BookComponent, Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final List<BookComponent> components = new ArrayList<>();

    public Category(String name) {
        this.name = name;
    }

    public void addComponent(BookComponent component) {
        components.add(component);
    }

    public List<BookComponent> getComponents() {
        return Collections.unmodifiableList(components);
    }

    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return components.stream().mapToDouble(BookComponent::getPrice).sum();
    }

    @Override
    public String toString() {
        return "📁 " + name;
    }
}
