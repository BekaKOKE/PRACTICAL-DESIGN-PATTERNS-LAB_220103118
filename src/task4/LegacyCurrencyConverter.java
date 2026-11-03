package task4;

public class LegacyCurrencyConverter {
    public double convert(double amount, String from, String to) {
        if ("GBP".equalsIgnoreCase(from) && "USD".equalsIgnoreCase(to)) {
            return amount * 1.25;
        }
        return amount;
    }

    public double convertRate(double amount, String fromCurr, String toCurr) {
        return convert(amount, fromCurr, toCurr);
    }
}
