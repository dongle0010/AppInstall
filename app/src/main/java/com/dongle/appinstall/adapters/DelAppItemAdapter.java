package com.dongle.appinstall.adapters;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dongle.appinstall.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DelAppItemAdapter extends RecyclerView.Adapter<DelAppItemAdapter.AppItemHolder> {
    private final List<PackageInfo> list;
    private final PackageManager packageManager;
    private SelectedItem listener;
    private boolean isSelect;

    public DelAppItemAdapter(PackageManager packageManager, List<PackageInfo> list) {
        this.list = list;
        this.packageManager = packageManager;
    }

    public void setOnSelectedListener(SelectedItem listener) {
        this.listener = listener;
    }

    public void isAllSelect(boolean isSelect) {
        this.isSelect = isSelect;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public AppItemHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new AppItemHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DelAppItemAdapter.AppItemHolder holder, int position) {
        PackageInfo packageInfo = list.get(position);
        String loadLabel = packageInfo.applicationInfo.loadLabel(packageManager).toString();
        Drawable loadIcon = packageInfo.applicationInfo.loadIcon(packageManager);
//        String packageName = packageInfo.packageName;
        holder.imageView.setImageDrawable(loadIcon);
        holder.textView.setText(loadLabel);
        if (isSelect) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSelected(holder.checkBox, packageInfo, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public interface SelectedItem {
        void onSelected(CheckBox checkBox, PackageInfo packageInfo, int position);
    }

    public class AppItemHolder extends RecyclerView.ViewHolder {

        private final TextView textView;
        private final ImageView imageView;
        private final CheckBox checkBox;

        public AppItemHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            textView = itemView.findViewById(R.id.text);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
