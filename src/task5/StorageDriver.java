package task5;

public interface StorageDriver {
    byte[] loadRawBlob(String resourceUri);
    String getStorageType();

    default String getStorageId() {
        return getStorageType();
    }
}
