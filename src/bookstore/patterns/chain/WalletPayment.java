package bookstore.patterns.chain;

public class WalletPayment extends PaymentHandler {
    private final double balance;

    public WalletPayment(double balance) {
        this.balance = balance;
    }

    @Override
    protected boolean internalCanPay(double amount) {
        return balance >= amount;
    }

    @Override
    protected boolean process(double amount) {
        return true;
    }

    @Override
    public String getName() {
        return "Wallet";
    }
}
