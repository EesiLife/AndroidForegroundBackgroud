package com.esilife.fbglib;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import java.io.File;
import java.lang.ref.WeakReference;


public class AppBgFgController implements Application.ActivityLifecycleCallbacks {

    //活动的进程数量 num of active process
    private int mActivityActiveProcCount = 0;
    //活动的Activity数量 num of active activity
    private int sActivityActiveCount = 0;
    
    private String statusPath;
    public File statusFile;
    private WeakReference<Context> mContext;

    private static class SingletonHolder{
        public static AppBgFgController instance = new AppBgFgController();
    }

    private AppBgFgController(){
        FBHandler.initHandler();
        statusPath = AppContext.getDataDir()+ "/status.proc";
        statusFile = new File(statusPath);
        if (null != statusFile && FBUtils.isMainProcess() && statusFile.exists()) {
            FBHandler.sendMessage(FBHandler.sCmd_Write_PID);
        }
    }

    public static AppBgFgController getInstance(){
        return SingletonHolder.instance;
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        mContext = new WeakReference<Context>(activity);
    }

    @Override
    public void onActivityStarted(final Activity activity) {
        mContext = new WeakReference<Context>(activity);
        if (mActivityActiveProcCount++ > 0) return;

    }

    @Override
    public void onActivityResumed(final Activity activity) {
        mContext = new WeakReference<Context>(activity);
        if (sActivityActiveCount++ > 0) return;
        FBHandler.remove(FBHandler.sCmd_App_BG);
        FBHandler.sendMessage(FBHandler.sCmd_App_FG);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        mContext = new WeakReference<Context>(activity);
        if (--sActivityActiveCount > 0) return;
        FBHandler.sendMessage(FBHandler.sCmd_App_BG, 3000);
    }

    @Override
    public void onActivityStopped(final Activity activity) {
        mContext = new WeakReference<Context>(activity);
        if (--mActivityActiveProcCount > 0) return;
        FBHandler.sendMessage(FBHandler.sCmd_App_BG);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }


    public void handleBgFgEvent(boolean fg) {
        try {
            int cacheFgPid = FBUtils.getValue(statusFile);
            int pid = android.os.Process.myPid();
            int uid = android.os.Process.myUid();
            if (fg) {
                if (pid != cacheFgPid) {//not self
                    FBUtils.writeToFile(statusFile, pid);
                    if (cacheFgPid <= 0 || FBUtils.checkPidActive(uid, cacheFgPid) <= 0) {
                        // other active process to fg, ignore
                        //app to fg
                        Intent i = new Intent();
                        i.setAction(FBBroadcastReceiver.FB_ACTION);
                        i.putExtra(FBBroadcastReceiver.FB_STATUS, true);
                        mContext.get().sendBroadcast(i);
                    }
                }
            } else if (sActivityActiveCount <= 0 && 0 != cacheFgPid) {
                if (cacheFgPid > 0) {
                    cacheFgPid = FBUtils.checkPidActive(uid, cacheFgPid);
                }
                if (pid == cacheFgPid || cacheFgPid <= 0) {
                    FBUtils.writeToFile(statusFile, 0);
                    //app to bg
                    Intent i = new Intent();
                    i.setAction(FBBroadcastReceiver.FB_ACTION);
                    i.putExtra(FBBroadcastReceiver.FB_STATUS, false);
                    mContext.get().sendBroadcast(i);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


}
