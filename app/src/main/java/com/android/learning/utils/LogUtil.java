package com.android.learning.utils;

import android.os.Process;
import android.util.Log;

import java.util.Locale;


public class LogUtil {

    private static final String TAG = "";
    // TODO :false
    private static boolean debug = false;

    private static final String LOG_MODE = "debug";
    private static final String LOG_SET = "allow";

    private static boolean openTraceInfo = true;

    public static void v(String msg) {
        v(TAG,msg);
    }

    public static void v(String tag, String msg) {
        v(tag, msg, null);
    }

    public static void v(String msg, Throwable tr) {
        v(TAG, msg, tr);
    }

    public static void v(Throwable tr) {
        v("",tr);
    }

    public static void v(String tag, String msg, Throwable tr) {
        if(isAllowOutput()){
            Log.v(tag, formatMessage(msg), tr);
        }
    }

    public static void d(String msg) {
        d(TAG, msg);
    }

    public static void d(String tag, String msg) {
        d(tag, msg, null);
    }

    public static void d(String msg, Throwable tr) {
        d(TAG, msg, tr);
    }

    public static void d(Throwable tr) {
        d("", tr);
    }

    public static void d(String tag, String msg, Throwable tr) {
        if(isAllowOutput()){
            Log.d(tag, formatMessage(msg), tr);
        }
    }

    public static void i(String msg) {
        i(TAG, msg);
    }

    public static void i(String tag, String msg) {
        i(tag, msg, null);
    }

    public static void i(String msg, Throwable tr) {
        i(TAG, msg, tr);
    }

    public static void i(Throwable tr) {
        i("", tr);
    }
    public static void i(String tag, String msg, Throwable tr) {
        if(isAllowOutput()){
            Log.i(tag, formatMessage(msg), tr);
        }
    }

    public static void w(String msg) {
        w(TAG, msg);
    }
    public static void w(String tag, String msg) {
        w(tag, msg, null);
    }

    public static void w(String msg, Throwable tr) {
        w(TAG, msg, tr);
    }

    public static void w(Throwable tr) {
        w("", tr);
    }
    public static void w(String tag, String msg, Throwable tr) {
        if(isAllowOutput()){
            Log.w(tag, formatMessage(msg), tr);
        }
    }

    public static void e(String msg) {
        e(TAG, msg);
    }

    public static void e(String tag, String msg) {
        e(tag, msg, null);
    }

    public static void e(String msg, Throwable tr) {
        e(TAG, msg, tr);
    }

    public static void e(Throwable tr) {
        e("", tr);
    }

    public static void e(String tag, String msg, Throwable tr) {
        if(isAllowOutput()){
            Log.e(tag, formatMessage(msg), tr);
        }
    }


    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean isDebugMode) {
        debug = isDebugMode;
    }

    public static String getTAG() {
        return TAG;
    }
//
//    public static void setTAG(String tag) {
//        TAG = tag;
//    }

    public static boolean isOpenTraceInfo(){
        return openTraceInfo;
    }

    public static void setOpenTraceInfo(boolean isOpen){
        openTraceInfo = isOpen;
    }


    private static boolean isAllowOutput(){
        boolean set = true;
        return debug||set;
    }

    private static String formatMessage(String msg) {
        if(isPrintStackTrace()){
            StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();
            String caller = "<unknown>";
            String lineNum = "<unknown>";
            for (int i = 0; i < trace.length; i++) {
                String callingClass = trace[i].getClassName();
                String callingClassName = callingClass.substring(callingClass.lastIndexOf('.') + 1);
                if (!callingClassName.equals(LogUtil.class.getSimpleName())) {
                    caller = callingClassName + "." + trace[i].getMethodName();
                    lineNum =  trace[i].getLineNumber()+"";
                    break;
                }
            }
            return String.format(Locale.CHINA, "[%d-%d-%s] %s: %s", Process.myPid(),Process.myTid(),lineNum,caller, msg);
        }else{
            return msg;
        }

    }

    private static boolean isPrintStackTrace(){
        return openTraceInfo;
    }

}
