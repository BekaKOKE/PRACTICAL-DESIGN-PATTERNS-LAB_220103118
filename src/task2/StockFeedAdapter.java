package task2;

import java.util.HashMap;
import java.util.Map;

public class StockFeedAdapter implements StockProvider {
    private final LegacyStockFeed legacyFeed;

    public StockFeedAdapter(LegacyStockFeed legacyFeed) {
        if (legacyFeed == null) throw new IllegalArgumentException("Legacy feed cannot be null");
        this.legacyFeed = legacyFeed;
    }

    private Map<String, String> parsePacket(String raw) {
        Map<String, String> map = new HashMap<>();
        if (raw == null) return map;
        String[] tokens = raw.split(";");
        for (String t : tokens) {
            String[] kv = t.split("=", 2);
            if (kv.length == 2 && !kv[0].trim().isEmpty()) {
                map.put(kv[0].trim().toUpperCase(), kv[1].trim());
            }
        }
        return map;
    }

    private double toDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    @Override
    public double getMarketPrice(String ticker) {
        String raw = legacyFeed.fetchRawTickerPacket(ticker);
        Map<String, String> parsed = parsePacket(raw);
        double bid = toDouble(parsed.getOrDefault("BID", "0.0"));
        double ask = toDouble(parsed.getOrDefault("ASK", "0.0"));
        return (bid + ask) / 2.0;
    }

    @Override
    public String getStatus(String ticker) {
        String raw = legacyFeed.fetchRawTickerPacket(ticker);
        return parsePacket(raw).getOrDefault("STATUS", "UNAVAILABLE");
    }
}
