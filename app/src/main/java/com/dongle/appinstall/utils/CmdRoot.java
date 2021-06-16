package com.dongle.appinstall.utils;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class CmdRoot {
    private static final String TAG = "CmdRoot";

    /**
     * 21      * APK静默安装
     * 22      *
     * 23      * @param apkPath
     * 24      *            APK安装包路径
     * 25      * @return true 静默安装成功 false 静默安装失败
     * 26
     */
    public static boolean install(String apkPath) {
        String[] args = {"pm", "install", "-r", apkPath};
        String result = apkProcess(args);
        Log.e(TAG, "install log:" + result);
        if (result != null
                && (result.endsWith("Success") || result.endsWith("Success\n"))) {
            return true;
        }
        return false;
    }

    /**
     * 57      * 应用安装、卸载处理
     * 58      *
     * 59      * @param args
     * 60      *            安装、卸载参数
     * 61      * @return Apk安装、卸载结果
     * 62
     */
    public static String apkProcess(String[] args) {
        String result = null;
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        Process process = null;
        InputStream errIs = null;
        InputStream inIs = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int read = -1;
            process = processBuilder.start();
            errIs = process.getErrorStream();
            while ((read = errIs.read()) != -1) {
                baos.write(read);
            }
            baos.write('\n');
            inIs = process.getInputStream();
            while ((read = inIs.read()) != -1) {
                baos.write(read);
            }
            byte[] data = baos.toByteArray();
            result = new String(data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (errIs != null) {
                    errIs.close();
                }
                if (inIs != null) {
                    inIs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (process != null) {
                process.destroy();
            }
        }
        return result;
    }
    /**
     * 39      * APK静默卸载
     * 40      *
     * 41      * @param packageName
     * 42      *            需要卸载应用的包名
     * 43      * @return true 静默卸载成功 false 静默卸载失败
     * 44
     */
    public static boolean uninstall(String packageName) {
        String[] args = {"pm", "uninstall", packageName};
        String result = apkProcess(args);
        Log.e(TAG, "uninstall log:" + result);
        if (result != null
                && (result.endsWith("Success") || result.endsWith("Success\n"))) {
            return true;
        }
        return false;
    }
}
