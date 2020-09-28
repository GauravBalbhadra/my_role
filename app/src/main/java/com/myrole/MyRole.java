package com.myrole;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

@ReportsCrashes(
        mailTo = "appmyrole@gmail.com",
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text, formKey = "MyRole")
public class MyRole extends MultiDexApplication {
    final static String TAG = "MYROLEAPP";
    private static MyRole myRole;

    public static MyRole getMyRoleApplication() {
        if (myRole == null) {
            myRole = new MyRole();
        }
        return myRole;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        //ACRA.init(this);
    }

   }
