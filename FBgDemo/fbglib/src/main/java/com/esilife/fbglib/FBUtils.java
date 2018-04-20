package com.esilife.fbglib;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;


public class FBUtils {
    private static final String TAG = FBUtils.class.getSimpleName();

    /**
     * @brief write int value to target file
     * @param file
     * @param value
     */
    public static void writeToFile(File file, int value){
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            os.write(String.valueOf(value).getBytes());
            os.flush();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            closeStream(os);
        }
    }

    /**
     * @brief get int value from target file
     * @param file
     * @return int
     */
    public static int getValue(File file){
        int result = 0;
        StringBuilder sb = new StringBuilder();
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
            byte[] temp = new byte[1024];
            int len = 0;
            while ((len = is.read(temp)) > 0) {
                sb.append(new String(temp, 0, len));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            closeStream(is);
        }
        if(!TextUtils.isEmpty(sb.toString())){
            result = string2Int(sb.toString());
        }
        return result;
    }

    /**
     * @brief: close stream
     * @param closeable
     */
    public static void closeStream(Closeable closeable){
        if (null != closeable) {
            try {
                closeable.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @brief: get Current Process Name
     * @return String: Current Process Name
     */
    private static String getCurrentProcessName() {
        int pid = android.os.Process.myPid();
        String processName = "";
        ActivityManager manager = (ActivityManager) AppContext.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
            if (process.pid == pid) {
                processName = process.processName;
            }
        }
        return processName;
    }

    /**
     * @brief: check target process is Main process
     * @return True: isMainProcess False: is not Main Process
     */
    public static boolean isMainProcess() {
        return AppContext.getPackageName().equals(getCurrentProcessName());
    }

    /***
     * @brief: check target pid is running
     * @param uid: target uid
     * @param pid: target pid
     * @return 0: process die, pid: active, otherwise error code
     * */
    public static int checkPidActive(int uid, int pid) {
        try {
            ActivityManager am = (ActivityManager)AppContext.getContext().getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> list = am.getRunningAppProcesses();
            if (null != list && list.size() > 0) {
                for (ActivityManager.RunningAppProcessInfo l : list) {
                    if (uid == l.uid && pid == l.pid) return pid;
                }
            }
            return 0;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return -9;
    }

    /**
     * @brief: String to Int
     * @param s String
     * @return int
     */
    public static int string2Int(String s){
        int result = 0;
        try {
            result = Integer.parseInt(s);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return result;
    }
}
