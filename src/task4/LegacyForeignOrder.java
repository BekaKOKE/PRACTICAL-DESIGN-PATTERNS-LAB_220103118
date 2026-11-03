package task4;

public class LegacyForeignOrder {
    private final String orderId;
    private final double gbpAmount;

    public LegacyForeignOrder(String orderId, double gbpAmount) {
        this.orderId = orderId;
        this.gbpAmount = gbpAmount;
    }

    public String getOrderId() { return orderId; }
    public double getGbpAmount() { return gbpAmount; }
}
