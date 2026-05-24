package bookstore.patterns.proxy;

import bookstore.patterns.state.Order;

public class UserProxy {
    private final String name;
    private boolean loggedIn;
    private User realUser;

    public UserProxy(String name, boolean loggedIn) {
        this.name = name;
        this.loggedIn = loggedIn;
    }

    public void login() {
        loggedIn = true;
        realUser = new User(name);
    }

    public void logout() {
        loggedIn = false;
        realUser = null;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public String getName() {
        return name;
    }

    public boolean placeOrder(Order order) {
        if (!loggedIn) {
            return false;
        }
        if (realUser == null) {
            realUser = new User(name);
        }
        return realUser.placeOrder(order);
    }
}
