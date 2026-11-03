package task5;

public class S3StorageDriver implements StorageDriver {
    @Override
    public byte[] loadRawBlob(String resourceUri) {
        System.out.printf("[AWS S3 CLIENT] Performing HTTPS GET on bucket uri: s3://%s%n", resourceUri);
        return new byte[]{0x1F, (byte) 0x8B, 0x08, 0x00};
    }

    @Override
    public String getStorageType() { return "AMAZON_S3_CLOUD"; }
}
