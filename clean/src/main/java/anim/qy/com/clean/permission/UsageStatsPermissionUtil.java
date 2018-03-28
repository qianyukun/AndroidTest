package anim.qy.com.clean.permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import anim.qy.com.clean.activity.AppUsagePermissionActivy;

/**
 * Created by Alter on 2018/3/28.
 */

public class UsageStatsPermissionUtil {

    @TargetApi(21)
    public static boolean startAppUsageSetting(Activity paramActivity, int paramInt) {
        try {
            paramActivity.startActivityForResult(new Intent("android.settings.USAGE_ACCESS_SETTINGS"), paramInt);
            return true;
        } catch (Exception localException) {
        }
        return false;
    }

    public static boolean checkAppUsagePermission(Context paramContext) {
        if (Build.VERSION.SDK_INT < 21)
            return true;
        try {
            int i = ((AppOpsManager) paramContext.getSystemService(Context.APP_OPS_SERVICE))
                    .checkOpNoThrow("android:get_usage_stats",
                            Process.myUid(), paramContext.getPackageName());
            if (i == 0)
                return true;
        } catch (Exception localException) {
        }
        return false;
    }

    public static void startPermissionGuide(final Activity paramContext) {
        new Handler(Looper.getMainLooper())
                .postDelayed(new Runnable() {
                                 public void run() {
                                     Intent localIntent = new Intent(paramContext, AppUsagePermissionActivy.class);
                                     localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                     paramContext.startActivity(localIntent);
                                 }
                             }
                        , 1000L);
    }

    public static String getForegroundApp(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            assert am != null;
            List<ActivityManager.RunningAppProcessInfo> lr = am.getRunningAppProcesses();
            if (lr == null) {
                return null;
            }
            for (ActivityManager.RunningAppProcessInfo ra : lr) {
                if (ra.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE
                        || ra.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return ra.processName;
                }
            }

        } else {
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(new Date());
            long endt = calendar.getTimeInMillis();//结束时间
            calendar.add(Calendar.MINUTE, -10);//时间间隔为一个小时
            long statt = calendar.getTimeInMillis();//开始时间
            UsageStatsManager usageStatsManager=(UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            //获取一个月内的信息
            List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_MONTHLY,statt,endt);

            if (queryUsageStats == null || queryUsageStats.isEmpty()) {
                return null;
            }

            UsageStats recentStats = null;
            for (UsageStats usageStats : queryUsageStats) {
                Log.e("Usage","PackageName : "+usageStats.getPackageName()
                        +"\tFirstTimeStamp : "+usageStats.getFirstTimeStamp()
                        +"\tLastTimeStamp : "+usageStats.getLastTimeStamp()
                        +"\tLastTimeUsed : "+usageStats.getLastTimeUsed()
                        +"\tTotalTimeInForeground : "+usageStats.getTotalTimeInForeground()
                );
                if(recentStats == null || recentStats.getLastTimeUsed() < usageStats.getLastTimeUsed()){
                    recentStats = usageStats;
                }
            }

            return recentStats.getPackageName();
        }

        return null;
    }


}
