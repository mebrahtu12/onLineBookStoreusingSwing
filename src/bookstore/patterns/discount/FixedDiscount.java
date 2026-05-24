package bookstore.patterns.discount;

public class FixedDiscount implements Discount {
    private final double amount;

    public FixedDiscount(double amount) {
        this.amount = amount;
    }

    @Override
    public double apply(double subtotal) {
        return Math.min(amount, subtotal);
    }

    @Override
    public String getDescription() {
        return "$" + String.format("%.2f", amount) + " off";
    }
}
