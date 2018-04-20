package com.esilife.fb;

import android.app.Application;
import android.content.Context;

import com.esilife.fbglib.AppContext;


public class FBApp extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        AppContext.init(base);
    }
}
