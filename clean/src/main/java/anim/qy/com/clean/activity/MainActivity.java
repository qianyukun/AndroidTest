package anim.qy.com.clean.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.Executors;

import anim.qy.com.clean.R;
import anim.qy.com.clean.permission.UsageStatsPermissionUtil;
import anim.qy.com.clean.task.GetPkgInfoTask;
import anim.qy.com.clean.util.StatusBarColorUtil;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_USAGESTATS = 1;

    MainHandler mainHandler = new MainHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        new GetPkgInfoTask(this).executeOnExecutor(Executors.newCachedThreadPool());
//        ActivityCompat.requestPermissions(this, new String[]{"android.permission.GET_PACKAGE_SIZE"},1);
        if (!UsageStatsPermissionUtil.checkAppUsagePermission(this)) {
            UsageStatsPermissionUtil.startAppUsageSetting(this, REQUEST_CODE_USAGESTATS);
            UsageStatsPermissionUtil.startPermissionGuide(this);
        } else {
            mainHandler.postDelayed(topApp,3000);
        }
    }

    Runnable topApp = new Runnable() {
        @Override
        public void run() {
            try {
                String foregroundApp = UsageStatsPermissionUtil.getForegroundApp(MainActivity.this);
                Log.e(MainActivity.this.getClass().getSimpleName(), "foregroundApp " + foregroundApp);
                StatusBarColorUtil.changeColor(MainActivity.this, Color.RED);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    static class MainHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(this.getClass().getSimpleName(), requestCode + " " + resultCode + " " + data);
        if (requestCode == REQUEST_CODE_USAGESTATS) {
            Log.e(this.getClass().getSimpleName(), "resultCde " + resultCode);
        }
    }
}
