package task2;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class Task2Test {
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

    public static void main(String[] args) throws InterruptedException {
        StockFeedAdapter adapter = new StockFeedAdapter(new LegacyStockFeed());
        check(adapter.getMarketPrice("AAPL") == 183.0, "AAPL mid price = 183.00");
        check(Math.abs(adapter.getMarketPrice("GOOGL") - 164.5) < 0.0001, "GOOGL mid price = 164.50");
        check(adapter.getMarketPrice("MSFT") == 0.0, "unknown ticker price = 0");
        check(adapter.getStatus("AAPL").equals("OPEN"), "AAPL status OPEN");
        check(adapter.getStatus("XYZ").equals("UNKNOWN"), "unknown ticker status UNKNOWN");

        StockFeedAdapter messy = new StockFeedAdapter(new LegacyStockFeed() {
            @Override
            public String fetchRawTickerPacket(String ticker) {
                return " TICKER = TST ;;BID=10.00; ASK =20.00 ;GARBAGE;STATUS=HALTED;";
            }
        });
        check(messy.getMarketPrice("TST") == 15.0, "messy packet parsed");
        check(messy.getStatus("TST").equals("HALTED"), "messy packet status parsed");

        PrintStream old = System.out;
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buf, true));
        CachedStockProxy proxy = new CachedStockProxy(adapter, 300);
        double p1 = proxy.getMarketPrice("AAPL");
        double p2 = proxy.getMarketPrice("AAPL");
        int callsHot = proxy.getUpstreamCalls();
        proxy.getMarketPrice("GOOGL");
        int callsOther = proxy.getUpstreamCalls();
        Thread.sleep(350);
        proxy.getMarketPrice("AAPL");
        int callsExpired = proxy.getUpstreamCalls();
        System.setOut(old);
        String out = buf.toString();

        check(p1 == 183.0 && p2 == 183.0, "proxy returns same price");
        check(callsHot == 1, "second request served from cache");
        check(callsOther == 2, "other ticker has its own cache entry");
        check(callsExpired == 3, "expired entry fetched again");
        check(out.contains("[CACHE HIT]"), "cache hit logged");
        check(out.contains("[NETWORK FETCH]"), "network fetch logged");

        boolean thrown = false;
        try {
            new CachedStockProxy(null, 100);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        check(thrown, "null provider rejected");

        System.out.println("Task2: " + passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }
}
