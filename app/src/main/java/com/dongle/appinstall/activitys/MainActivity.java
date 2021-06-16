package com.dongle.appinstall.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.dongle.appinstall.R;
import com.dongle.appinstall.adapters.DelAppItemAdapter;
import com.dongle.appinstall.databinding.ActivityMainBinding;
import com.dongle.appinstall.utils.CmdRoot;
import com.dongle.appinstall.utils.RootCmd;
import com.dongle.appinstall.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DelAppItemAdapter delAppItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("app卸载");
        @NonNull ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.recycleList.setLayoutManager(new LinearLayoutManager(this));
        AddListItem();
        delAppItemAdapter = new DelAppItemAdapter(getPackageManager(), orginList);
        binding.recycleList.setAdapter(delAppItemAdapter);
        delAppItemAdapter.setOnSelectedListener(new DelAppItemAdapter.SelectedItem() {

            @Override
            public void onSelected(CheckBox checkBox, PackageInfo packageInfo, int position) {
                if (checkBox.isChecked()) {
                    packageInfos.remove(packageInfo);
                    checkBox.setChecked(false);
                    ToastUtil.showTip("已移除");
                } else {
                    packageInfos.add(packageInfo);
                    checkBox.setChecked(true);
                    ToastUtil.showTip("已添加");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        CheckBox checkbox = (CheckBox) menu.findItem(R.id.allcheck).getActionView();
        checkbox.setText("全选");
        checkbox.setChecked(false);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkbox.isChecked()) {
                    checkbox.setText("取消");
                    checkbox.setChecked(true);
                    packageInfos.addAll(orginList);
                    delAppItemAdapter.isAllSelect(true);
                } else {
                    checkbox.setText("全选");
                    packageInfos.clear();
                    delAppItemAdapter.isAllSelect(false);
                    checkbox.setChecked(false);
                }
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                new DEleteApk().execute();
                break;
        }
        return true;
    }

    private void updateList() {
        for (PackageInfo packageInfo : packageInfos) {
            orginList.remove(packageInfo);
        }
        packageInfos.clear();
        delAppItemAdapter.notifyDataSetChanged();
    }

    List<PackageInfo> orginList = new ArrayList<>();

    private List<PackageInfo> AddListItem() {
        orginList.clear();
        PackageManager packageManager = getPackageManager();
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (PackageInfo packageInfo : installedPackages) {
            int flags = packageInfo.applicationInfo.flags;//标识
            if((ApplicationInfo.FLAG_SYSTEM & flags) ==0){
                orginList.add(packageInfo);
                //用户程序
//                appInfo.setUserApp(true);
//            }else{
                //系统程序
//                appInfo.setUserApp(false);
            }
        }
        return orginList;
    }

    List<PackageInfo> packageInfos = new ArrayList<>();

    class DEleteApk extends AsyncTask {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("正在卸载中");
            progressDialog.setMessage("请稍后...");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            for (PackageInfo packageInfo : packageInfos) {
                RootCmd.unInstallApk(packageInfo.packageName);
//                CmdRoot.uninstall(packageInfo.packageName);
            }
            return true;
        }

        @Override
        protected void onPostExecute(Object o) {
            progressDialog.dismiss();
            updateList();
        }
    }
}