package com.esilife.fb;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;

import com.esilife.fbglib.AppBgFgController;
import com.esilife.fbglib.AppContext;
import com.esilife.fbglib.FBBroadcastReceiver;


public class FBApp extends Application {
    private FBBroadcastReceiver mFBBroadcastReceiver;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        AppContext.init(base);
        registerActivityLifecycleCallbacks(AppBgFgController.getInstance());
        if (null == mFBBroadcastReceiver) {
            mFBBroadcastReceiver = new FBBroadcastReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(FBBroadcastReceiver.FB_ACTION);
            registerReceiver(mFBBroadcastReceiver, intentFilter);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (null != mFBBroadcastReceiver) {
            unregisterReceiver(mFBBroadcastReceiver);
        }
    }
}
