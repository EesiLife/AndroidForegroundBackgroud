package com.esilife.fbglib;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;


public class FBHandler implements Handler.Callback {
//    private static final String TAG = FBHandler.class.getSimpleName();

    public static final int sCmd_App_BG = 1;
    public static final int sCmd_App_FG = 2;
    public static final int sCmd_Write_PID = 3;

    private void doHandleMessage(Message msg) {
//        android.util.Log.e(TAG, "doHandleMessage" + msg.what);

        switch (msg.what) {
            case sCmd_App_BG:
                AppBgFgController.getInstance().handleBgFgEvent(false);
                break;
            case sCmd_App_FG:
                AppBgFgController.getInstance().handleBgFgEvent(true);
                break;
            case sCmd_Write_PID:
                FBUtils.writeToFile(AppBgFgController.getInstance().statusFile, 0);
                break;
            default: break;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        try {
            doHandleMessage(msg);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return true;
    }

    private FBHandler() {
        mThread = new HandlerThread("fb.handler");
        mThread.start();
        mHandler = new Handler(mThread.getLooper(), this);
    }

    private final Handler mHandler;
    private final HandlerThread mThread;
    private static FBHandler sFBH = null;
    private static Handler sUiH = null;
    public static void initHandler() {
        if (null == sFBH) {
            try {
                sFBH = new FBHandler();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        if (null == sUiH) {
            try {
                sUiH = new Handler(Looper.getMainLooper());
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static void sendMessage(int what) {
        sendMessage(what, 0);
    }

    public static void sendMessage(int what, Object obj) {
        sendMessage(what, obj, 0);
    }

    public static void sendMessage(int what, Object obj, long delay) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = obj;
        sendMessage(msg, delay);
    }

    public static void sendMessage(int what, long delay) {
        Message msg = Message.obtain();
        msg.what = what;
        sendMessage(msg, delay);
    }

    public static void sendMessage(Message msg) {
        sendMessage(msg, 0);
    }

    public static void sendMessage(Message msg, long delay) {
        if (null == sFBH) return ;
        if (delay <= 0) {
            sFBH.mHandler.sendMessage(msg);
        } else {
            sFBH.mHandler.sendMessageDelayed(msg, delay);
        }
    }

    public static void post(Runnable b) {
        if(null == sFBH)return;
        sFBH.mHandler.post(b);
    }

    public static void post(Runnable b, long delay) {
        if(null == sFBH)return;
        if (delay <= 0) {
            sFBH.mHandler.post(b);
        } else {
            sFBH.mHandler.postDelayed(b, delay);
        }
    }

    public static void postUi(Runnable b) {
        if(null == sUiH)return;
        sUiH.post(b);
    }

    public static void postUi(Runnable b, long delay) {
        if(null == sUiH)return;
        if (delay <= 0) {
            sUiH.post(b);
        } else {
            sUiH.postDelayed(b, delay);
        }
    }

    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    public static void remove(int what){
        if (null == sFBH) return ;
        sFBH.mHandler.removeMessages(what);
    }

    public static void removeMsgAndCallbacks(){
        if(null == sFBH)return;
        sFBH.mHandler.removeCallbacksAndMessages(null);
    }

}
