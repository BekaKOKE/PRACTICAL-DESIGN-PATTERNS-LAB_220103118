package task4;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CustomsDutyDecorator extends PricingDecorator {
    private static final BigDecimal RATE = new BigDecimal("1.085");

    public CustomsDutyDecorator(PricingService wrapped) { super(wrapped); }

    @Override
    public double calculateTotal() {
        BigDecimal subtotal = BigDecimal.valueOf(super.calculateTotal());
        return subtotal.multiply(RATE).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    @Override
    public String getReceiptBreakdown() {
        return super.getReceiptBreakdown() + " + [Customs Duty: 8.5%]";
    }
}
