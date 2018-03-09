package com.android.learning.classloader;

import android.content.Context;

import com.android.learning.utils.LogUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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
        test1();
        test2();
        test3();
    }

    private void test1(){
        try {
            Class<?> hookClass = Class.forName("com.android.learning.utils.LogUtil");
            Object hookedObj = hookClass.newInstance();
            Method log_e = hookClass.getMethod("e",String.class);
            log_e.invoke(hookedObj,"asd");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void test2(){
        String name = "caterpillar";
        Class stringClass = name.getClass();
        LogUtil.e("类名：" +
                stringClass.getName());
        LogUtil.e("是否为接口：" +
                stringClass.isInterface());
        LogUtil.e("是否为基本类型：" +
                stringClass.isPrimitive());
        LogUtil.e("是否数组：" +
                stringClass.isArray());
        LogUtil.e("父类名：" +
                stringClass.getSuperclass().getName());
    }

    //--------proxy test start
    interface Subject{
        void request();
    }

    class RealSubject implements Subject{

        @Override
        public void request() {
            LogUtil.e("run request()");
        }
    }

    class ProxyHandler implements InvocationHandler{
        private Subject subject;

        public ProxyHandler(Subject subject) {
            this.subject = subject;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            LogUtil.e("==before==");
            Object result = method.invoke(subject,args);
            LogUtil.e("==end==");
            return result;
        }
    }

    private void test3(){
        RealSubject realSubject = new RealSubject();
        ProxyHandler handler = new ProxyHandler(realSubject);
        Subject proxySubject = (Subject) Proxy.newProxyInstance(RealSubject.class.getClassLoader(),
                RealSubject.class.getInterfaces(),
                handler);
        proxySubject.request();
    }

    //proxy test end


}
