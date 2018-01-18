package permission;

/**
 * Created by Alter on 2018/1/3.
 */

public interface IPermissionCallback {
    void requesting(int requestCode);
    void granted(int requestCode);
    void denied(int requestCode);
    void alreadyGranted(int requestCode);
}
