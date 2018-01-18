package learning.android.com.androidlearning;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.learning.classloader.ClassLoaderManager;
import com.android.learning.download.DownloadApkManager;

import java.util.HashMap;
import java.util.Map;

import permission.IPermissionCallback;
import permission.PermissionManager;

public class MainActivity extends AppCompatActivity {

    private PermissionManager permissionManager;
    private Map<Integer,String> permissionMap;
    private ClassLoaderManager classLoaderManager;

    public static final class RequestCode{
        public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;
        public static final int PERMISSIONS_REQUEST_INSTALL_SHORTCUT = 2;
        public static final int PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 3;
        public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 4;
        public static final int PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE = 5;
        public static final int PERMISSIONS_REQUEST_ACCESS_WIFI_STATE = 6;
        public static final int PERMISSIONS_REQUEST_CHANGE_WIFI_STATE = 7;
        public static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 8;
        public static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 9;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPermissionModel();
        initClassLoaderModel();
        findViewById(R.id.notification_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permissionManager.requestPermission(MainActivity.this,RequestCode.PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                permissionManager.requestPermission(MainActivity.this,RequestCode.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        });
    }

    private void initPermissionModel(){
        permissionManager = new PermissionManager();

        permissionMap = new HashMap<>();
        permissionMap.put(RequestCode.PERMISSIONS_REQUEST_READ_CONTACTS, Manifest.permission.READ_CONTACTS);
        permissionMap.put(RequestCode.PERMISSIONS_REQUEST_INSTALL_SHORTCUT, Manifest.permission.INSTALL_SHORTCUT);
        permissionMap.put(RequestCode.PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionMap.put(RequestCode.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
        permissionMap.put(RequestCode.PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_NETWORK_STATE);
        permissionMap.put(RequestCode.PERMISSIONS_REQUEST_ACCESS_WIFI_STATE, Manifest.permission.ACCESS_WIFI_STATE);
        permissionMap.put(RequestCode.PERMISSIONS_REQUEST_CHANGE_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE);
        permissionMap.put(RequestCode.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
        permissionMap.put(RequestCode.PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        permissionManager.initPermissionConstants(permissionMap);
        permissionManager.init(this,new IPermissionCallback(){
            @Override
            public void requesting(int requestCode) {

            }

            @Override
            public void granted(int requestCode) {
                afterGranted(requestCode);
            }

            @Override
            public void denied(int requestCode) {

            }

            @Override
            public void alreadyGranted(int requestCode) {
                afterGranted(requestCode);
            }
        });
    }

    private void initClassLoaderModel(){
        classLoaderManager = ClassLoaderManager.getInstance(MainActivity.this);
        classLoaderManager.run();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionManager.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    private void afterGranted(int requestCode){
        switch (requestCode) {
            case RequestCode.PERMISSIONS_REQUEST_READ_CONTACTS: {
                return;
            }
            case RequestCode.PERMISSIONS_REQUEST_INSTALL_SHORTCUT: {

                return;
            }
            case RequestCode.PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                DownloadApkManager.download(MainActivity.this);
                break;
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
