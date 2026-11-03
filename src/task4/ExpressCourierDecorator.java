package task4;

public class ExpressCourierDecorator extends PricingDecorator {
    public ExpressCourierDecorator(PricingService wrapped) { super(wrapped); }

    @Override
    public double calculateTotal() {
        return super.calculateTotal() + 15.00;
    }

    @Override
    public String getReceiptBreakdown() {
        return super.getReceiptBreakdown() + " + [Express Courier: $15.00]";
    }
}
