package com.dongle.appinstall.utils;

import android.content.pm.PackageInstaller;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.InputStream;

//adb命令翻译执行类
public class RootCmd {
    /***
     * @param command
     * @return
     */
    public static boolean exusecmd(String command) {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            Log.e("updateFile", "======000==writeSuccess======");
            process.waitFor();
        } catch (Exception e) {
            Log.e("updateFile", "======111=writeError======" + e.toString());
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static void unInstallApk(String pageName){
        exusecmd("pm uninstall "+pageName);
    }
    public static void installApk(String appPath){

        exusecmd("pm  install "+appPath);
    }

}