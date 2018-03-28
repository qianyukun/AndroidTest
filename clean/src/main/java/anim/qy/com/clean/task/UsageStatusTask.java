package anim.qy.com.clean.task;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import anim.qy.com.clean.bean.AppInfo;

/**
 * Created by Alter on 2018/3/27.
 */

public class UsageStatusTask extends AsyncTask<Void, Integer, List<AppInfo>> {
    private int mAppCount = 0;
    private Context mContext;

    private void getPackageUseInfo(String pkgName,IPackageStatsObserver observer){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                ((AppOpsManager) mContext.getSystemService(Context.APP_OPS_SERVICE))
                        .checkOpNoThrow("android:get_usage_stats",
                                Process.myUid(), mContext.getPackageName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UsageStatusTask(Context context) {
        this.mContext = context;
        Log.e("clean",mContext.getApplicationInfo().targetSdkVersion+"");
    }

    @Override
    protected List<AppInfo> doInBackground(Void... params) {
        PackageManager pm = mContext.getPackageManager();
        List<PackageInfo> packInfos = pm.getInstalledPackages(0);
        publishProgress(0, packInfos.size());
        List<AppInfo> appinfos = new ArrayList<AppInfo>();
        for (PackageInfo packInfo : packInfos) {
            publishProgress(++mAppCount, packInfos.size());
            final AppInfo appInfo = new AppInfo();
            Drawable appIcon = packInfo.applicationInfo.loadIcon(pm);
            appInfo.setAppIcon(appIcon);

            int flags = packInfo.applicationInfo.flags;

            int uid = packInfo.applicationInfo.uid;

            appInfo.setUid(uid);

            if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                appInfo.setUserApp(false);//系统应用
            } else {
                appInfo.setUserApp(true);//用户应用
            }
            if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
                appInfo.setInRom(false);
            } else {
                appInfo.setInRom(true);
            }
            String appName = packInfo.applicationInfo.loadLabel(pm).toString();
            appInfo.setAppName(appName);
            String packname = packInfo.packageName;
            appInfo.setPackname(packname);
            String version = packInfo.versionName;
            appInfo.setVersion(version);
            try {
                getPackageUseInfo( packname,
                        new IPackageStatsObserver.Stub() {
                            @Override
                            public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
                                synchronized (appInfo) {
                                    appInfo.setPkgSize(pStats.cacheSize + pStats.codeSize + pStats.dataSize);
                                }
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }

            appinfos.add(appInfo);
        }
        return appinfos;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
//            try {
//                mProgressBarText.setText(getString(R.string.scanning_m_of_n, values[0], values[1]));
//            } catch (Exception e) {
//
//            }

    }

    @Override
    protected void onPreExecute() {
//            try {
//                showProgressBar(true);
//                mProgressBarText.setText(R.string.scanning);
//            } catch (Exception e) {
//
//            }

        //    loading.setVisibility(View.VISIBLE);
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<AppInfo> result) {

        super.onPostExecute(result);


        try {


//                userAppInfos = new ArrayList<>();
//                systemAppInfos = new ArrayList<>();
//                long allSize = 0;
            for (AppInfo a : result) {
                if (a.getPkgSize()>0)
                    Log.e("Clean ", a.toString());
//                    if (a.isUserApp()) {
//                        allSize += a.getPkgSize();
//                        userAppInfos.add(a);
//                    } else {
//                        systemAppInfos.add(a);
//                    }
            }
//                if (position == 0) {
//                    topText.setText(getString(R.string.software_top_text, userAppInfos.size(), StorageUtil.convertStorage(allSize)));
//                    mAutoStartAdapter = new SoftwareAdapter(mContext, userAppInfos);
//                    listview.setAdapter(mAutoStartAdapter);
//
//                } else {
//
//                    mAutoStartAdapter = new SoftwareAdapter(mContext, systemAppInfos);
//                    listview.setAdapter(mAutoStartAdapter);
//
//                }
        } catch (Exception e) {

        }
    }

}
