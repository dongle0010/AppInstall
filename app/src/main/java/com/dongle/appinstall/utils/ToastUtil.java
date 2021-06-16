package com.dongle.appinstall.utils;

import android.widget.Toast;

import com.dongle.appinstall.IApplication;

public class ToastUtil {
    public static void showTip(String str) {
        Toast.makeText(IApplication.applicationContext, str, Toast.LENGTH_SHORT).show();
    }
}
