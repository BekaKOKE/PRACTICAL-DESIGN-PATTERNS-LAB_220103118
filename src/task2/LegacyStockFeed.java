package task2;

public class LegacyStockFeed {
    public String fetchRawTickerPacket(String ticker) {
        try { Thread.sleep(250); } catch (InterruptedException ignored) {}
        if ("AAPL".equalsIgnoreCase(ticker)) {
            return "TICKER=AAPL;BID=182.50;ASK=183.50;STATUS=OPEN;TIMESTAMP=17300000";
        } else if ("GOOGL".equalsIgnoreCase(ticker)) {
            return "TICKER=GOOGL;BID=164.20;ASK=164.80;STATUS=OPEN;TIMESTAMP=17300000";
        }
        return "TICKER=" + ticker + ";BID=0.00;ASK=0.00;STATUS=UNKNOWN;TIMESTAMP=0";
    }
}
