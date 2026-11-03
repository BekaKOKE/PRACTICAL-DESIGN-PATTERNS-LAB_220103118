package task5;

public class NfsStorageDriver implements StorageDriver {
    @Override
    public byte[] loadRawBlob(String resourceUri) {
        System.out.printf("[NFS POSIX CLIENT] Performing direct IO read on /mnt/nfs/%s%n", resourceUri);
        return new byte[]{0x4F, 0x67, 0x67, 0x53};
    }

    @Override
    public String getStorageType() { return "LOCAL_NFS_CLUSTER"; }
}
