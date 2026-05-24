package bookstore.patterns.discount;

public class PercentageDiscount implements Discount {
    private final double percent;

    public PercentageDiscount(double percent) {
        this.percent = percent;
    }

    @Override
    public double apply(double subtotal) {
        return subtotal * (percent / 100.0);
    }

    @Override
    public String getDescription() {
        return String.format("%.0f%% off", percent);
    }
}
