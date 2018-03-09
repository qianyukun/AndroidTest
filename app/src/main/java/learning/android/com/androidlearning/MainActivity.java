package learning.android.com.androidlearning;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import com.android.learning.classloader.ClassLoaderManager;
import com.android.learning.download.DownloadApkManager;
import com.android.learning.utils.LogUtil;
import com.blankj.utilcode.util.FileUtils;
import com.proxy.interfaces.toast.IToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import dalvik.system.DexClassLoader;
import permission.IPermissionCallback;
import permission.PermissionManager;

public class MainActivity extends AppCompatActivity {

    private PermissionManager permissionManager;
    private Map<Integer, String> permissionMap;
    private ClassLoaderManager classLoaderManager;

    public static final class RequestCode {
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
        //xiaomi 沉浸式
//        {
//            Class clazz = window.getClass();
//            try {
//                int tranceFlag = 0;
//                int darkModeFlag = 0;
//                Class layoutParams = null;
//                layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
//                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_TRANSPARENT");
//                tranceFlag = field.getInt(layoutParams);
//
//                field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
//                darkModeFlag = field.getInt(layoutParams);
//
//                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class); //只需要状态栏透明
//                extraFlagField.invoke(window, tranceFlag, tranceFlag); //或
//                // 状态栏透明且黑色字体 extraFlagField.invoke(window, tranceFlag | darkModeFlag, tranceFlag | darkModeFlag);
//                // 清除黑色字体
//                extraFlagField.invoke(window, 0, darkModeFlag);
//            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            } catch (NoSuchFieldException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (IllegalArgumentException e) {
//                e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            }
//        }
        setContentView(R.layout.activity_main);
        initPermissionModel();
        initClassLoaderModel();
        findViewById(R.id.notification_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permissionManager.requestPermission(MainActivity.this, RequestCode.PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                permissionManager.requestPermission(MainActivity.this, RequestCode.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        });
        findViewById(R.id.splash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SplashImmersiveStickyActivity.class));
            }
        });
    }

    private void initPermissionModel() {
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
        permissionManager.init(this, new IPermissionCallback() {
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

    private void initClassLoaderModel() {
        classLoaderManager = ClassLoaderManager.getInstance(MainActivity.this);
        classLoaderManager.run();
    }

    @SuppressLint("StaticFieldLeak")
    private void initDexLoaderModel() {
//        new NativeToastImpl().build(this,"initDexLoaderModel success ",1000).show();
        new AsyncTask<Void, Void, DexClassLoader>() {
            @Override
            protected DexClassLoader doInBackground(Void... voids) {
                final File destDir = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "androidTest");
                final File destFile = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "androidTest"
                        + File.separator
                        + "plugin.apk");
                FileUtils.createOrExistsDir(destDir);
                FileUtils.createOrExistsFile(destFile);
                try {
                    InputStream is = null;
                    is = getAssets().open("plugin.apk");
                    FileOutputStream fos = new FileOutputStream(destFile);
                    byte[] buffer = new byte[7168];
                    int count = 0;
                    while ((count = is.read(buffer)) > 0) {
                        fos.write(buffer, 0, count);
                    }
                    fos.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String opDirectory = getDir("plugin", 0).getAbsolutePath();
                DexClassLoader dexClassLoader = new DexClassLoader(
                        destFile.getAbsolutePath(),
                        opDirectory,
                        null, MainActivity.this.getClassLoader());

                return dexClassLoader;
            }

            @Override
            protected void onPostExecute(DexClassLoader dexClassLoader) {
                super.onPostExecute(dexClassLoader);
                try {
                    Class<?> nativeToastImpl = dexClassLoader.loadClass("com.proxy.iml.toast.NativeToastImpl");
                    LogUtil.e(nativeToastImpl.getName());
                    try {
                        IToast nativeToast = (IToast) nativeToastImpl.newInstance();
                        nativeToast
                                .build(MainActivity.this, "dex Toast!", 100)
                                .show();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    Class<?> bClass = dexClassLoader.loadClass("com.proxy.interfaces.toast.IToast");
                    LogUtil.e(bClass.getName());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }.execute();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void afterGranted(int requestCode) {
        switch (requestCode) {
            case RequestCode.PERMISSIONS_REQUEST_READ_CONTACTS: {
                return;
            }
            case RequestCode.PERMISSIONS_REQUEST_INSTALL_SHORTCUT: {

                return;
            }
            case RequestCode.PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                DownloadApkManager.download(MainActivity.this);
                initDexLoaderModel();
                break;
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
