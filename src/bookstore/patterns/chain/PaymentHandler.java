package bookstore.patterns.chain;

public abstract class PaymentHandler {
    private PaymentHandler next;

    public void setNext(PaymentHandler next) {
        this.next = next;
    }

    PaymentHandler getNext() {
        return next;
    }

    public boolean handlePayment(double amount) {
        if (internalCanPay(amount) && process(amount)) {
            return true;
        }
        if (next != null) {
            return next.handlePayment(amount);
        }
        return false;
    }

    public boolean canPay(double amount) {
        return internalCanPay(amount);
    }

    protected abstract boolean internalCanPay(double amount);

    protected abstract boolean process(double amount);

    public abstract String getName();
}
