package permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import java.util.Map;

/**
 * Created by Alter on 2018/1/3.
 */

public abstract class AbstactPermissionManager {
    Activity context;
    private IPermissionCallback callback;

    public void setCallback(IPermissionCallback callback) {
        this.callback = callback;
    }

    public IPermissionCallback getCallback() {
        return callback;
    }

    public Activity getContext() {
        return context;
    }

    public void setContext(Activity context) {
        this.context = context;
    }

    public void init(Activity context,IPermissionCallback callback){
        this.context =context;
        this.callback = callback;
    }

    public abstract void requestPermission(Activity context, int requestCode);

    public abstract void shouldShowRequestPermissionRationale(int requestCode);

    public abstract void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

    public abstract boolean caseRequestCode(int requestCode, @NonNull int[] grantResults);

    public boolean chechPermissionGranted(Activity context, int requestCode) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
            return ContextCompat.checkSelfPermission(context,
                    PermissionConstants.permissionMap.get(requestCode))
                    != PackageManager.PERMISSION_GRANTED;
        else
            return context.checkCallingPermission(PermissionConstants.permissionMap.get(requestCode))
                    != PackageManager.PERMISSION_GRANTED;
    }

    public void initPermissionConstants(Map<Integer ,String > permissionMap){
        for (Map.Entry entry :permissionMap.entrySet()){
            PermissionConstants.RequestCodes.add((Integer) entry.getKey());
            PermissionConstants.permissionMap.put((Integer) entry.getKey(),(String) entry.getValue());
        }
    }
}
