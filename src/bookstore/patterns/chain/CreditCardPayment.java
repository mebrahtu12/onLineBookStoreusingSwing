package bookstore.patterns.chain;

public class CreditCardPayment extends PaymentHandler {
    private final double limit;

    public CreditCardPayment(double limit) {
        this.limit = limit;
    }

    @Override
    protected boolean internalCanPay(double amount) {
        return limit >= amount;
    }

    @Override
    protected boolean process(double amount) {
        return true;
    }

    @Override
    public String getName() {
        return "Credit Card";
    }
}
