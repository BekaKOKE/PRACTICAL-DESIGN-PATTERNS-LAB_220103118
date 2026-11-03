package task1;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64EncryptDecorator extends ExporterDecorator {

    public Base64EncryptDecorator(Exporter wrappedExporter) {
        super(wrappedExporter);
    }

    @Override
    protected String process(String data) {
        String encoded = Base64.getEncoder().encodeToString(data.getBytes(StandardCharsets.UTF_8));
        return "[BASE64_CIPHERTEXT:" + encoded + "]";
    }
}
