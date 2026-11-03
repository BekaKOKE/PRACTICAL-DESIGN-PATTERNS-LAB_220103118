package task4;

public class Task4Test {
    static int passed = 0;
    static int failed = 0;

    static void check(boolean condition, String name) {
        if (condition) {
            passed++;
            System.out.println("[PASS] " + name);
        } else {
            failed++;
            System.out.println("[FAIL] " + name);
        }
    }

    static boolean eq(double a, double b) {
        return Math.abs(a - b) < 0.0001;
    }

    public static void main(String[] args) {
        LegacyCurrencyConverter converter = new LegacyCurrencyConverter();
        check(eq(converter.convert(100, "GBP", "USD"), 125.0), "100 GBP = 125 USD");
        check(eq(converter.convertRate(10, "gbp", "usd"), 12.5), "convertRate works");

        PricingService base = new CurrencyOrderAdapter(new LegacyForeignOrder("ORD-9821", 100.00), converter, "USD");
        check(eq(base.calculateTotal(), 125.0), "adapter converts to USD");
        check(base.getReceiptBreakdown().equals("Order [ORD-9821] Base Item: $125.00 USD (Converted from 100.00 GBP)"), "adapter receipt");

        PricingService full = new CustomsDutyDecorator(new ExpressCourierDecorator(new GiftWrapDecorator(base)));
        check(eq(full.calculateTotal(), 157.33), "full checkout = 157.33");
        check(full.getReceiptBreakdown().endsWith("+ [Gift Wrap: $5.00] + [Express Courier: $15.00] + [Customs Duty: 8.5%]"), "receipt lists all fees");

        check(eq(new GiftWrapDecorator(base).calculateTotal(), 130.0), "gift wrap +5");
        check(eq(new ExpressCourierDecorator(base).calculateTotal(), 140.0), "express +15");
        check(eq(new CustomsDutyDecorator(base).calculateTotal(), 135.63), "customs 8.5% on 125");

        PricingService taxFirst = new GiftWrapDecorator(new CustomsDutyDecorator(base));
        check(eq(taxFirst.calculateTotal(), 140.63), "order of decorators matters for percentage");

        PricingService doubleWrap = new GiftWrapDecorator(new GiftWrapDecorator(base));
        check(eq(doubleWrap.calculateTotal(), 135.0), "same decorator can be stacked twice");

        boolean thrown = false;
        try {
            new GiftWrapDecorator(null);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        check(thrown, "null service rejected");

        System.out.println("Task4: " + passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }
}
