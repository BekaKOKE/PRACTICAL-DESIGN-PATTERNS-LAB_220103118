package task1;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Task1Test {
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

    static String capture(Runnable r) {
        PrintStream old = System.out;
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buf, true));
        try {
            r.run();
        } finally {
            System.setOut(old);
        }
        return buf.toString().replace("\r\n", "\n");
    }

    public static void main(String[] args) {
        String[] headers = {"ID", "User", "Role"};
        String[][] rows = {{"101", "sufyan", "Admin"}, {"102", "alex", "Dev"}};

        String json = new JsonFormatter().format("UserAuditReport", headers, rows);
        check(json.startsWith("{\n  \"report\": \"UserAuditReport\""), "json has report title");
        check(json.contains("{\"ID\": \"101\", \"User\": \"sufyan\", \"Role\": \"Admin\"},"), "json first record");
        check(json.endsWith("  ]\n}"), "json closed properly");

        String escaped = new JsonFormatter().format("T", new String[]{"Name"}, new String[][]{{"a\"b"}});
        check(escaped.contains("\"a\\\"b\""), "json escapes quotes");

        String csv = new CsvFormatter().format("UserAuditReport", headers, rows);
        check(csv.equals("# TITLE: UserAuditReport\n\"ID\",\"User\",\"Role\"\n\"101\",\"sufyan\",\"Admin\"\n\"102\",\"alex\",\"Dev\"\n"), "csv quoted rows");
        String csvComma = new CsvFormatter().format("T", new String[]{"A"}, new String[][]{{"x,\"y\""}});
        check(csvComma.contains("\"x,\"\"y\"\"\""), "csv escapes commas and quotes");

        final String data = json;
        String out = capture(() -> new Base64EncryptDecorator(
                new HeaderFooterDecorator(new BaseFileExporter(), "2026-11-02 10:00:00 UTC")).export("/var/logs/audit.sec", data));
        check(out.contains("[BASE64_CIPHERTEXT:PT09IE1FVEFEQVRBIEhFQURFUjogMjAyNi0xMS0wMiAxMDowMDowMCBVVEMgPT09CnsKICAicmVwb3J0IjogIlVzZXJBdWRpdFJlcG9ydCIsCiAgInJlY29yZ"), "base64 prefix matches expected trace");
        String encoded = out.substring(out.indexOf("[BASE64_CIPHERTEXT:") + 19, out.indexOf("]\n----- FILE PAYLOAD END"));
        String decoded = new String(Base64.getDecoder().decode(encoded), StandardCharsets.UTF_8);
        check(decoded.equals("=== METADATA HEADER: 2026-11-02 10:00:00 UTC ===\n" + json + "\n=== INTEGRITY CHECK: SHA-256 OK | END RECORD ==="), "base64 decodes to header + json + footer");
        check(out.contains("file:///var/logs/audit.sec"), "destination printed");

        String reversed = capture(() -> new HeaderFooterDecorator(
                new Base64EncryptDecorator(new BaseFileExporter()), "TS").export("x.txt", "hello"));
        check(reversed.contains("=== METADATA HEADER: TS ===\n[BASE64_CIPHERTEXT:aGVsbG8=]\n=== INTEGRITY CHECK"), "decorators can be stacked in other order");

        String plain = capture(() -> new BaseFileExporter().export("plain.txt", "abc"));
        check(plain.contains("Writing 3 bytes") && plain.contains("\nabc\n"), "base exporter unchanged");

        boolean thrown = false;
        try {
            new Base64EncryptDecorator(null);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        check(thrown, "null wrapped exporter rejected");

        System.out.println("Task1: " + passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }
}
