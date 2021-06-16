package com.dongle.appinstall;

import android.app.Application;
import android.content.Context;

public class IApplication extends Application {
    public static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(applicationContext);
    }
}
