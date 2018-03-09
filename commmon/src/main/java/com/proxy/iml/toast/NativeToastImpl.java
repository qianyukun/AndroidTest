package com.proxy.iml.toast;

import android.content.Context;
import android.widget.Toast;

import com.proxy.interfaces.toast.IToast;

/**
 * Created by Alter on 2018/1/19.
 */

public class NativeToastImpl implements IToast {
    Toast toast;
    @Override
    public void show() {
        toast.show();
    }

    @Override
    public IToast build(Context context,String msg, int time) {
        toast = Toast.makeText(context,msg,time);
        return this;
    }
}
