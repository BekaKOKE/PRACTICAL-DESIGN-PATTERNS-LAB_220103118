package task4;

public class GiftWrapDecorator extends PricingDecorator {
    public GiftWrapDecorator(PricingService wrapped) { super(wrapped); }

    @Override
    public double calculateTotal() {
        return super.calculateTotal() + 5.00;
    }

    @Override
    public String getReceiptBreakdown() {
        return super.getReceiptBreakdown() + " + [Gift Wrap: $5.00]";
    }
}
