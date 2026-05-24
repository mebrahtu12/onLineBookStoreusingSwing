package bookstore.patterns.state;

public class ConfirmedState implements OrderState {
    @Override
    public String getName() {
        return "Confirmed";
    }

    @Override
    public OrderState next() {
        return new ShippedState();
    }
}
