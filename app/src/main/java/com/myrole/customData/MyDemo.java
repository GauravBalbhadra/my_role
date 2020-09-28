package com.myrole.customData;

import android.app.Application;
import android.content.Context;

public class MyDemo extends Application {

    private static Context appContext;
    public static boolean VERBOSE = false;
    public static boolean DEBUG = true;

    public void onCreate() {
        super.onCreate();
        // TypefaceUtil.overrideFont(getApplicationContext(), "serif", "varela_regular.ttf");//"fonts/varela_regular.ttf");
        appContext = getApplicationContext();

        /* If you has other classes that need context object to initialize when application is created,
         * you can use the appContext here to process. */
    }

    public static Context getAppContext() {
        return appContext;
    }
}

