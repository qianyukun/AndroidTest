package com.android.learning.download;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import learning.android.com.androidlearning.R;

/**
 * Created by Alter on 2018/1/3.
 */

public class DownloadApkManager {
    public static void download(final Activity activity){
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setTitle("软件升级")
                .setMessage("发现新版本,建议立即更新使用.")
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //开启更新服务UpdateService
                        //这里为了把update更好模块化，可以传一些updateService依赖的值
                        //如布局ID，资源ID，动态获取的标题,这里以app_name为例
                        Intent updateIntent =new Intent(activity, UpdateService.class);
                        updateIntent.putExtra("titleId", R.string.app_name);
                        activity.startService(updateIntent);
                    }
                })
                .setNegativeButton("取消",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alert.create().show();
    }
}
