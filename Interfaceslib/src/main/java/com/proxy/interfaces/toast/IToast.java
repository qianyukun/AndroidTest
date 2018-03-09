package com.proxy.interfaces.toast;

import android.content.Context;

/**
 * Created by Alter on 2018/1/19.
 */

public interface IToast {
    void show();

    IToast build(Context context,String msg, int time);
}
