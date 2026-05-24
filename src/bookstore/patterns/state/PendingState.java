package bookstore.patterns.state;

public class PendingState implements OrderState {
    @Override
    public String getName() {
        return "Pending";
    }

    @Override
    public OrderState next() {
        return new ConfirmedState();
    }
}
