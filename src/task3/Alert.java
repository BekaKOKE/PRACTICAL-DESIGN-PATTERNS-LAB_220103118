package task3;

public abstract class Alert {
    protected final MessageChannel channel;
    protected final DispatchStrategy strategy;

    public Alert(MessageChannel channel, DispatchStrategy strategy) {
        if (channel == null || strategy == null) throw new IllegalArgumentException("Dependencies cannot be null");
        this.channel = channel;
        this.strategy = strategy;
    }

    public void processAlert(String recipient, String rawContent, int currentHour) {
        send(recipient, rawContent, getSeverityLevel(), currentHour);
    }

    public void send(String recipient, String message, int severity, int hour) {
        if (hour < 0 || hour > 23) throw new IllegalArgumentException("Hour must be between 0 and 23");
        if (strategy.shouldDispatch(severity, hour)) {
            channel.transmit(recipient, formatAlertBody(message));
        } else {
            System.out.printf("[DISPATCH SUPPRESSED] Alert blocked by policy '%s' at hour %02d:00 (Severity: %d)%n",
                    strategy.getStrategyName(), hour, severity);
        }
    }

    public abstract int getSeverityLevel();
    public abstract String formatAlertBody(String raw);
}
