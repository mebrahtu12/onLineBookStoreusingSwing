package bookstore.patterns.mediator;

import bookstore.patterns.cart.ShoppingCart;
import bookstore.patterns.chain.PaymentSystem;
import bookstore.patterns.inventory.Inventory;
import bookstore.patterns.state.Order;

public class BookstoreMediator {
    private ShoppingCart cart;
    private Inventory inventory;
    private PaymentSystem paymentSystem;
    private Order currentOrder;

    public void setColleagues(ShoppingCart cart, Inventory inventory, PaymentSystem paymentSystem) {
        this.cart = cart;
        this.inventory = inventory;
        this.paymentSystem = paymentSystem;
        cart.setMediator(this);
        inventory.setMediator(this);
        paymentSystem.setMediator(this);
    }

    public void setCurrentOrder(Order order) {
        this.currentOrder = order;
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public PaymentSystem getPaymentSystem() {
        return paymentSystem;
    }

    public void notifyCheckout(double amount) {
        if (paymentSystem != null) {
            paymentSystem.processPayment(amount);
        }
    }
}
