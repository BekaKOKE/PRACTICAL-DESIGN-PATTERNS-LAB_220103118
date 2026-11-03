package task5;

public abstract class DigitalAsset {
    protected final StorageDriver driver;
    protected final String assetPath;

    public DigitalAsset(StorageDriver driver, String assetPath) {
        if (driver == null) throw new IllegalArgumentException("Driver cannot be null");
        this.driver = driver;
        this.assetPath = assetPath;
    }

    public abstract void render();
}
