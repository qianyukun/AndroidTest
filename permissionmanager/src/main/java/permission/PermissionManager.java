package permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Alter on 2018/1/3.
 */

public class PermissionManager extends AbstactPermissionManager {

    @Override
    public void requestPermission(Activity context, int requestCode) {
        if (chechPermissionGranted(context, requestCode)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                    PermissionConstants.permissionMap.get(requestCode))) {
                shouldShowRequestPermissionRationale(requestCode);
                ActivityCompat.requestPermissions(context,
                        new String[]{PermissionConstants.permissionMap.get(requestCode)},
                        requestCode);
            } else {

                getCallback().requesting(requestCode);
                ActivityCompat.requestPermissions(context,
                        new String[]{PermissionConstants.permissionMap.get(requestCode)},
                        requestCode);
            }
        } else {
            getCallback().alreadyGranted(requestCode);
        }
    }

    @Override
    public void shouldShowRequestPermissionRationale(int requestCode) {
        Log.e(this.getClass().toString(), "shouldShowRequestPermissionRationale");
        Toast.makeText(context, "shouldShowRequestPermissionRationale " + PermissionConstants.permissionMap.get(requestCode), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        caseRequestCode(requestCode, grantResults);

    }

    @Override
    public boolean caseRequestCode(int requestCode, @NonNull int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            // permission was granted, yay! Do the
            // contacts-related task you need to do.

            getCallback().granted(requestCode);
            return true;
        } else {

            // permission denied, boo! Disable the
            // functionality that depends on this permission.
            getCallback().denied(requestCode);
            return false;
        }
    }
}
