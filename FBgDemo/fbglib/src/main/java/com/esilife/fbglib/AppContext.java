package com.esilife.fbglib;


import android.content.Context;
import android.content.pm.ApplicationInfo;

public class AppContext {
    private static Context mAppContext = null;
    private static String mDataDir = null;
    private static String mPackageName = null;

    public static void init(Context context) {
        mAppContext = context;
    }

    public static Context getContext() {
        return mAppContext;
    }

    public static String getDataDir() {
        if (null == mDataDir) {
            getAppInfo();
        }
        return mDataDir;
    }

    private static void getAppInfo() {
        try {
            ApplicationInfo ai = getContext().getPackageManager().getApplicationInfo(getPackageName(), 0);
            if (null != ai) {
                mDataDir = ai.dataDir;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static String getPackageName() {
        if (null == mPackageName) {
            mPackageName = getContext().getPackageName();
        }
        return mPackageName;
    }
}
