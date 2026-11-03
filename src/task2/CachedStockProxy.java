package task2;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CachedStockProxy implements StockProvider {

    private static class CacheEntry {
        final double price;
        final long timestamp;

        CacheEntry(double price, long timestamp) {
            this.price = price;
            this.timestamp = timestamp;
        }
    }

    private final StockProvider realProvider;
    private final long ttlMillis;
    private final Map<String, CacheEntry> priceCache = new HashMap<>();
    private int upstreamCalls = 0;

    public CachedStockProxy(StockProvider realProvider, long ttlMillis) {
        if (realProvider == null) throw new IllegalArgumentException("Real provider cannot be null");
        this.realProvider = realProvider;
        this.ttlMillis = ttlMillis;
    }

    @Override
    public double getMarketPrice(String ticker) {
        long now = System.currentTimeMillis();
        CacheEntry entry = priceCache.get(ticker);
        if (entry != null && (now - entry.timestamp) < ttlMillis) {
            System.out.printf(Locale.US, "[CACHE HIT] Returning cached price for %s: $%.2f (Age: %d ms)%n",
                    ticker, entry.price, (now - entry.timestamp));
            return entry.price;
        }
        System.out.printf("[NETWORK FETCH] Cache miss or expired for %s. Calling upstream adapter...%n", ticker);
        upstreamCalls++;
        double freshPrice = realProvider.getMarketPrice(ticker);
        priceCache.put(ticker, new CacheEntry(freshPrice, System.currentTimeMillis()));
        return freshPrice;
    }

    @Override
    public String getStatus(String ticker) {
        return realProvider.getStatus(ticker);
    }

    public int getUpstreamCalls() {
        return upstreamCalls;
    }
}
