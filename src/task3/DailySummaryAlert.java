package task3;

public class DailySummaryAlert extends Alert {
    public DailySummaryAlert(MessageChannel channel, DispatchStrategy strategy) {
        super(channel, strategy);
    }

    @Override
    public int getSeverityLevel() { return 1; }

    @Override
    public String formatAlertBody(String raw) {
        return "Daily Digest:\n" + raw;
    }
}
