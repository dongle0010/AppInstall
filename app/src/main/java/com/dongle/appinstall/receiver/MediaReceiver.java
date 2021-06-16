package com.dongle.appinstall.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.dongle.appinstall.activitys.APKListAct;


public class MediaReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
//        ToastUtil.showTip("广播来了:" + intent.getAction());
        switch (intent.getAction()) {
            case Intent.ACTION_MEDIA_CHECKING:
                break;
            case Intent.ACTION_MEDIA_MOUNTED:
//                ToastUtil.showTip("U盘工具&&&U盘插入！");
// 获取挂载路径, 读取U盘文件
                try {
                    Uri uri = intent.getData();
                    if (uri != null) {
                        String filePath = uri.getPath();
                        Intent intent1 = new Intent(context, APKListAct.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent1.putExtra("apkFilePath", filePath);
                        context.startActivity(intent1);
                        //                    for (File file : rootFile.listFiles()) {
                        //// 文件列表，进行相应操作...
                        //                        if (file.getName().equals(U_DISK_FILE_NAME)) {
                        //                            Intent install = new Intent(Intent.ACTION_VIEW);
                        ////设置intent的数据类型是应用程序application
                        //                            install.setDataAndType(Uri.parse("file://" + file.toString()), "application/vnd.android.package-archive");
                        ////为这个新apk开启一个新的activity栈
                        //                            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //                            context.startActivity(install);
                        //                            break;
                        //                        }
                        //                    }
                    }
                } catch (Exception e) {
//                    ToastUtil.showTip(e.getMessage().toString());
                }
                break;
            case Intent.ACTION_MEDIA_EJECT:
                break;

            case Intent.ACTION_MEDIA_REMOVED:
            case Intent.ACTION_MEDIA_UNMOUNTED:
//                ToastUtil.showTip("U盘工具&&&&U盘拔出！！");
                break;
        }
    }
}