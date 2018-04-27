package com.esilife.fbglib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.esilife.fbglib.nofity.FBObserverable;

/**
 * Created by siy on 18-4-27.
 */
public class FBBroadcastReceiver extends BroadcastReceiver {
    public final static String FB_ACTION = "com.esilife.fbglib.fb";
    public final static String FB_STATUS = "fb_status";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (null != intent) {
            boolean fg = intent.getBooleanExtra(FB_STATUS, false);
            FBObserverable.getInstance().setStatus(fg);
        }
    }
}
