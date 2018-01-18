package com.android.learning.classloader;

import android.content.Context;

import com.android.learning.utils.LogUtil;

/**
 * Created by Alter on 2018/1/10.
 */

public class ClassLoaderManager {
    private Context context;
    private static ClassLoaderManager instance;

    private ClassLoaderManager(Context context) {
        this.context = context;
    }

    public static ClassLoaderManager getInstance(Context context) {

        if (instance == null)
            synchronized (ClassLoaderManager.class) {
                if (instance == null) {
                    instance = new ClassLoaderManager(context);
                    LogUtil.i("ClassLoaderManager init");
                }
            }
        return instance;
    }

    public void run(){
        LogUtil.e("ClassLoaderManager run");
    }


}
