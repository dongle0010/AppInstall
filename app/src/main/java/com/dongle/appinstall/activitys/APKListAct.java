package com.dongle.appinstall.activitys;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dongle.appinstall.R;
import com.dongle.appinstall.adapters.DelAppItemAdapter;
import com.dongle.appinstall.adapters.InstAppItemAdapter;
import com.dongle.appinstall.databinding.ActivityMainBinding;
import com.dongle.appinstall.utils.RootCmd;
import com.dongle.appinstall.utils.ToastUtil;
import com.dongle.appinstall.databinding.ActAddapkinfoBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class APKListAct extends AppCompatActivity {
    List<PackageInfo> orginList = new ArrayList<>();
    List<String> pathOrginList = new ArrayList<>();
    List<String> pathNewList = new ArrayList<>();
    private InstAppItemAdapter instAppItemAdapter;
    private PackageManager pm;
    private String apkFilePath;
    private ActAddapkinfoBinding binding;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("app安装");
        pm = getPackageManager();
        @NonNull ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        apkFilePath = getIntent().getStringExtra("apkFilePath");
        binding.recycleList.setLayoutManager(new LinearLayoutManager(this));
        if (isStoragePermissionGranted()) {
            AddListItem();
        }
        instAppItemAdapter = new InstAppItemAdapter(getPackageManager(), pathNewList, orginList);
        binding.recycleList.setAdapter(instAppItemAdapter);
        instAppItemAdapter.setOnSelectedListener(new InstAppItemAdapter.SelectedItem() {
            @Override
            public void onSelected(CheckBox checkBox, String packagePath, int position) {
                if (checkBox.isChecked()) {
                    pathNewList.remove(packagePath);
                    checkBox.setChecked(false);
                    ToastUtil.showTip("已移除");
                } else {
                    pathNewList.add(packagePath);
                    checkBox.setChecked(true);
                    ToastUtil.showTip("已添加");
                }
            }
        });
//
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            final Context context = getApplicationContext();
            int readPermissionCheck = ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            int writePermissionCheck = ContextCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (readPermissionCheck == PackageManager.PERMISSION_GRANTED
                    && writePermissionCheck == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            AddListItem();

        }
    }

    //    private void updateList() {
//        for (PackageInfo packageInfo : packageInfos) {
//            orginList.remove(packageInfo);
//        }
//        packageInfos.clear();
//        delAppItemAdapter.notifyDataSetChanged();
//    }

    private void AddListItem() {
        try {
            pathOrginList.clear();
            orginList.clear();
            File rootFile = new File(apkFilePath + "/ceshi/");
            if (rootFile != null) {
                File[] files = rootFile.listFiles();
                if (files == null) {
                    ToastUtil.showTip("找不到文档列表");
                    return;
                }
                for (File file : files) {
                    String name = file.getName();
                    if (name.endsWith(".apk")) {
                        String path = file.getPath();
                        PackageInfo packageArchiveInfo = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
                        orginList.add(packageArchiveInfo);
                        pathOrginList.add(path);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar2, menu);
        CheckBox checkbox = (CheckBox) menu.findItem(R.id.allcheck).getActionView();
        checkbox.setText("全选");
        checkbox.setChecked(false);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkbox.isChecked()) {
                    checkbox.setText("取消");
                    checkbox.setChecked(true);
                    pathNewList.addAll(pathOrginList);
                    instAppItemAdapter.isAllSelect(true);
                } else {
                    checkbox.setText("全选");
                    pathNewList.clear();
                    instAppItemAdapter.isAllSelect(false);
                    checkbox.setChecked(false);
                }
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.install_btn:
                new IntallApk().execute();
                break;
        }
        return true;
    }

    class IntallApk extends AsyncTask {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(APKListAct.this);
            progressDialog.setTitle("正在卸载中");
            progressDialog.setMessage("请稍后...");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            for (String apppath : pathNewList) {
                RootCmd.installApk(apppath);
            }
            return true;
        }

        @Override
        protected void onPostExecute(Object o) {
            progressDialog.dismiss();
            orginList.clear();
        }
    }
}
