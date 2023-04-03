package com.carl.testapplicationjava;

import android.app.Application;
import android.content.Context;

import com.carl.testapplicationjava.utils.LogUtils;

import io.reactivex.plugins.RxJavaPlugins;

/**
 * @author Carl
 * @version 1.0
 * @since 2018/5/10
 */
public class BaseApplication extends Application {

    public static String base64;

    private static Context mContext;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        RxJavaPlugins.setErrorHandler(throwable -> {
            throwable.printStackTrace();
            LogUtils.e("RxJavaPlugins unknown exception: " + throwable.getMessage());
        });
    }


    public static Context getContext() {
        return mContext;
    }

}
