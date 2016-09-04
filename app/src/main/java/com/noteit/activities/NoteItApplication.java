package com.noteit.activities;

import android.app.Application;
import android.content.Context;

/**
 * Created by rdanino on 8/6/2016.
 */
public class NoteItApplication extends Application {

    protected static NoteItApplication mInstance;

    public static Context getContext() {
        return mInstance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);
    }
}
