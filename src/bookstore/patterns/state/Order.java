package bookstore.patterns.state;

import bookstore.patterns.mediator.BookstoreMediator;

public class Order {
    private OrderState state = new PendingState();
    private final BookstoreMediator mediator;

    public Order(BookstoreMediator mediator) {
        this.mediator = mediator;
    }

    public OrderState getState() {
        return state;
    }

    public void confirm() {
        if (state instanceof PendingState) {
            state = state.next();
        }
    }

    public void nextState() {
        state = state.next();
    }

    public String getStatusLine() {
        return state.getName();
    }
}
