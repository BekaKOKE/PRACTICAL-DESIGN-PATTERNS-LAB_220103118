package task5;

public class AudioAsset extends DigitalAsset {
    public AudioAsset(StorageDriver driver, String assetPath) {
        super(driver, assetPath);
    }

    @Override
    public void render() {
        System.out.printf("Initializing Audio Stream for: %s%n", assetPath);
        byte[] payload = driver.loadRawBlob(assetPath);
        if (payload.length > 0) {
            System.out.printf("SUCCESS: Audio bitstream synchronized %d bytes via %s%n", payload.length, driver.getStorageType());
        } else {
            System.out.println("FAILURE: Audio stream inaccessible.");
        }
    }
}
