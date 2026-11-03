package task5;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class Task5Test {
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

    static class CountingDriver implements StorageDriver {
        int calls = 0;

        @Override
        public byte[] loadRawBlob(String resourceUri) {
            calls++;
            return new byte[]{1, 2, 3};
        }

        @Override
        public String getStorageType() { return "TEST_DRIVER"; }
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
        CountingDriver real = new CountingDriver();
        final byte[][] result = new byte[1][];

        StorageDriver guest = new StorageDriverProxy(real, new UserSession("anon", "GUEST"));
        String out = capture(() -> result[0] = guest.loadRawBlob("a.mp4"));
        check(result[0].length == 0, "guest gets empty payload");
        check(real.calls == 0, "guest request never reaches real driver");
        check(out.startsWith("[PROXY BLOCKED]"), "guest block logged");

        StorageDriver noRole = new StorageDriverProxy(real, new UserSession("x", null));
        capture(() -> result[0] = noRole.loadRawBlob("a.mp4"));
        check(result[0].length == 0 && real.calls == 0, "missing role blocked");

        StorageDriver fake = new StorageDriverProxy(real, new UserSession("x", "SUPERUSER"));
        capture(() -> result[0] = fake.loadRawBlob("a.mp4"));
        check(result[0].length == 0 && real.calls == 0, "unknown role blocked");

        StorageDriver member = new StorageDriverProxy(real, new UserSession("bob", "MEMBER"));
        out = capture(() -> result[0] = member.loadRawBlob("a.mp4"));
        check(result[0].length == 3 && real.calls == 1, "member allowed");
        check(out.startsWith("[PROXY AUDIT] Authorized user") && out.contains("[Role: MEMBER] accessing backend: TEST_DRIVER"), "audit logged");

        check(member.getStorageType().equals("PROXY_GUARDED(TEST_DRIVER)"), "proxy storage type");
        check(new S3StorageDriver().getStorageId().equals("AMAZON_S3_CLOUD"), "s3 storage id");
        check(new NfsStorageDriver().getStorageId().equals("LOCAL_NFS_CLUSTER"), "nfs storage id");

        StorageDriver admin = new StorageDriverProxy(new NfsStorageDriver(), new UserSession("root", "ADMIN"));
        out = capture(() -> new VideoAsset(admin, "v.mp4").render());
        check(out.contains("/mnt/nfs/v.mp4") && out.contains("SUCCESS: Video playback buffered 4 bytes via PROXY_GUARDED(LOCAL_NFS_CLUSTER)"), "video works with nfs");

        out = capture(() -> new AudioAsset(new S3StorageDriver(), "s.flac").render());
        check(out.contains("s3://s.flac") && out.contains("SUCCESS: Audio bitstream synchronized 4 bytes via AMAZON_S3_CLOUD"), "audio works with s3 directly");

        out = capture(() -> new VideoAsset(guest, "v.mp4").render());
        check(out.contains("FAILURE: Video buffer empty. Playback aborted."), "video fails for guest");

        boolean thrown = false;
        try {
            new StorageDriverProxy(real, null);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        check(thrown, "null session rejected");

        System.out.println("Task5: " + passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }
}
