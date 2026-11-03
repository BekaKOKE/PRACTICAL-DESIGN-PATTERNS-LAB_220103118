package task5;

public class VideoAsset extends DigitalAsset {
    public VideoAsset(StorageDriver driver, String assetPath) {
        super(driver, assetPath);
    }

    @Override
    public void render() {
        System.out.printf("Initializing Video Player for: %s%n", assetPath);
        byte[] payload = driver.loadRawBlob(assetPath);
        if (payload.length > 0) {
            System.out.printf("SUCCESS: Video playback buffered %d bytes via %s%n", payload.length, driver.getStorageType());
        } else {
            System.out.println("FAILURE: Video buffer empty. Playback aborted.");
        }
    }
}
