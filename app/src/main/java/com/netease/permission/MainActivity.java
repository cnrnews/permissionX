package com.netease.permission;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.permission.annotation.Permission;
import com.netease.permission.annotation.PermissionCancel;
import com.netease.permission.annotation.PermissionDenied;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void requestPermission(View view) {
        permissionRequest();
    }

    @Permission(value = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, requestCode = 200)
    public void permissionRequest() {
        Toast.makeText(this, "申请成功", Toast.LENGTH_SHORT).show();
    }

    @PermissionCancel
    public void permissionCancel() {
        Toast.makeText(this, "权限被取消", Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied
    public void permissionDenied() {
        Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
    }

}
