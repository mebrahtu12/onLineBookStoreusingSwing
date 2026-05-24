package bookstore.patterns.proxy;

import bookstore.patterns.state.ConfirmedState;
import bookstore.patterns.state.Order;

public class User {
    private final String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean placeOrder(Order order) {
        order.confirm();
        return order.getState() instanceof ConfirmedState;
    }
}
