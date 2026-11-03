package task5;

public class Task5Runner {
    public static void main(String[] args) {
        // Physical Implementors
        StorageDriver cloudS3 = new S3StorageDriver();
        StorageDriver localNfs = new NfsStorageDriver();

        UserSession adminUser = new UserSession("sufyan", "ADMIN");
        UserSession guestUser = new UserSession("anonymous", "GUEST");

        System.out.println(">>> 1. Authorized Video Playback via S3 (Admin Role) <<<");
        StorageDriver authorizedProxy = new StorageDriverProxy(cloudS3, adminUser);
        DigitalAsset video = new VideoAsset(authorizedProxy, "trailers/hyperion_4k.mp4");
        video.render();

        System.out.println("\n>>> 2. Unauthorized Audio Streaming via NFS (Guest Role - Blocked) <<<");
        StorageDriver blockedProxy = new StorageDriverProxy(localNfs, guestUser);
        DigitalAsset audio = new AudioAsset(blockedProxy, "master_audio/soundtrack.flac");
        audio.render();
    }
}
