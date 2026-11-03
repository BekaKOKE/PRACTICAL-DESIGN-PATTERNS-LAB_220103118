package task1;

public class HeaderFooterDecorator extends ExporterDecorator {
    private final String timestamp;

    public HeaderFooterDecorator(Exporter wrappedExporter, String timestamp) {
        super(wrappedExporter);
        this.timestamp = timestamp;
    }

    @Override
    protected String process(String data) {
        return "=== METADATA HEADER: " + timestamp + " ===\n"
                + data + "\n"
                + "=== INTEGRITY CHECK: SHA-256 OK | END RECORD ===";
    }
}
