package com.myrole.utils;

import android.content.Context;
import android.content.Intent;

import com.myrole.WelcomeActivity;
import com.myrole.dashboard.MainDashboardActivity;

public class ActivityTransactions {

    public static void getLogIn(Context context) {
        context.startActivity(new Intent(context, WelcomeActivity.class));
    }

    public static void getDashboard(Context context) {
        context.startActivity(new Intent(context, MainDashboardActivity.class));
    }
}
