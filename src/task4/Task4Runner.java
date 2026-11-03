package task4;

public class Task4Runner {
    public static void main(String[] args) {
        LegacyForeignOrder order = new LegacyForeignOrder("ORD-9821", 100.00); // 100 GBP -> 125 USD
        LegacyCurrencyConverter converter = new LegacyCurrencyConverter();

        PricingService baseOrder = new CurrencyOrderAdapter(order, converter, "USD");
        System.out.printf("Base Converted: $%.2f%n", baseOrder.calculateTotal());

        // Decorate: Base + GiftWrap ($5) + Express ($15) -> $145.00 -> Customs 8.5% -> $157.33
        PricingService completeCheckout = new CustomsDutyDecorator(
            new ExpressCourierDecorator(
                new GiftWrapDecorator(baseOrder)
            )
        );

        System.out.println("\n>>> INVOICE BREAKDOWN <<<");
        System.out.println(completeCheckout.getReceiptBreakdown());
        System.out.printf("FINAL CHARGE TOTAL: $%.2f%n", completeCheckout.calculateTotal());
    }
}
