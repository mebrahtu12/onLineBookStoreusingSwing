package bookstore.patterns.chain;

import bookstore.patterns.mediator.BookstoreMediator;

public class PaymentSystem {
    private BookstoreMediator mediator;
    private final PaymentHandler handlerChain;
    private String lastMethodUsed = "—";

    public PaymentSystem(BookstoreMediator mediator, PaymentHandler handlerChain) {
        this.mediator = mediator;
        this.handlerChain = handlerChain;
    }

    public void setMediator(BookstoreMediator mediator) {
        this.mediator = mediator;
    }

    public boolean processPayment(double amount) {
        PaymentHandler current = handlerChain;
        while (current != null) {
            if (current.canPay(amount) && current.process(amount)) {
                lastMethodUsed = current.getName();
                return true;
            }
            current = current.getNext();
        }
        lastMethodUsed = "None";
        return false;
    }

    public String getLastMethodUsed() {
        return lastMethodUsed;
    }
}
