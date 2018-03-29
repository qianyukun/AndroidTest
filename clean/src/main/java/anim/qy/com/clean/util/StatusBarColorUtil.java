package anim.qy.com.clean.util;

import android.app.Activity;
import android.os.Build;
import android.view.WindowManager;

public class StatusBarColorUtil {

    public static void changeColor(Activity paramActivity, int paramInt1) {
        if (Build.VERSION.SDK_INT >= 21) {
            paramActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            paramActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            paramActivity.getWindow().setStatusBarColor(paramInt1);
        }
    }
}