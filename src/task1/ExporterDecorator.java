package task1;

public abstract class ExporterDecorator implements Exporter {
    protected final Exporter wrappedExporter;

    public ExporterDecorator(Exporter wrappedExporter) {
        if (wrappedExporter == null) {
            throw new IllegalArgumentException("Wrapped exporter cannot be null");
        }
        this.wrappedExporter = wrappedExporter;
    }

    protected abstract String process(String data);

    private String processChain(String data) {
        String result = data;
        if (wrappedExporter instanceof ExporterDecorator) {
            result = ((ExporterDecorator) wrappedExporter).processChain(data);
        }
        return process(result);
    }

    private Exporter target() {
        if (wrappedExporter instanceof ExporterDecorator) {
            return ((ExporterDecorator) wrappedExporter).target();
        }
        return wrappedExporter;
    }

    @Override
    public void export(String destination, String data) {
        target().export(destination, processChain(data));
    }
}
