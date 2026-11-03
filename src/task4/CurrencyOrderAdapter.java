package task4;

import java.util.Locale;

public class CurrencyOrderAdapter implements PricingService {
    private final LegacyForeignOrder foreignOrder;
    private final LegacyCurrencyConverter converter;
    private final String targetCurrency;

    public CurrencyOrderAdapter(LegacyForeignOrder order, LegacyCurrencyConverter converter, String targetCurrency) {
        if (order == null || converter == null || targetCurrency == null) {
            throw new IllegalArgumentException("Order, converter and currency cannot be null");
        }
        this.foreignOrder = order;
        this.converter = converter;
        this.targetCurrency = targetCurrency;
    }

    @Override
    public double calculateTotal() {
        return converter.convert(foreignOrder.getGbpAmount(), "GBP", targetCurrency);
    }

    @Override
    public String getReceiptBreakdown() {
        return String.format(Locale.US, "Order [%s] Base Item: $%.2f %s (Converted from %.2f GBP)",
                foreignOrder.getOrderId(), calculateTotal(), targetCurrency, foreignOrder.getGbpAmount());
    }
}
