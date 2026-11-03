package task4;

public abstract class PricingDecorator implements PricingService {
    protected final PricingService wrappedService;

    public PricingDecorator(PricingService wrappedService) {
        if (wrappedService == null) throw new IllegalArgumentException("Wrapped service cannot be null");
        this.wrappedService = wrappedService;
    }

    @Override
    public double calculateTotal() {
        return wrappedService.calculateTotal();
    }

    @Override
    public String getReceiptBreakdown() {
        return wrappedService.getReceiptBreakdown();
    }
}
