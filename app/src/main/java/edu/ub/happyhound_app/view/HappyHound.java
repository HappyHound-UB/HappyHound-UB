package edu.ub.happyhound_app.view;

import android.app.Application;

import com.google.android.material.color.DynamicColors;

public class HappyHound extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DynamicColors.applyToActivitiesIfAvailable(this);

    }
}
