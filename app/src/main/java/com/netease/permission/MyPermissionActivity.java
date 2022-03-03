package com.netease.permission;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.netease.permission.core.IPermission;
import com.netease.permission.util.PermissionUtils;

/**
 * 用于权限申请的Activity，也是整个权限申请框架的核心类，对用户不可见
 */
public class MyPermissionActivity extends AppCompatActivity {

    // 从用户端传递过来的权限申请数组
    private static final String PARAM_PERMISSION = "param_permission";
    // 用于权限申请回调的 code
    private static final String PARAM_REQUEST_CODE = "param_request_code";

    public static final int PARAM_REQUEST_CODE_DEFAULT = -1;

    // 权限列表
    private String[] permissions;
    private int requestCode;
    // 权限申请回调
    private static IPermission mIPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_permission);

        permissions = getIntent().getStringArrayExtra(PARAM_PERMISSION);
        requestCode = getIntent().getIntExtra(PARAM_REQUEST_CODE, PARAM_REQUEST_CODE_DEFAULT);

        // 前置条件
        if (permissions == null || requestCode < 0 || mIPermission == null) {
            this.finish();
            return;
        }

        boolean permissionRequest = PermissionUtils.hasPermissionRequest(this, permissions);
        if (permissionRequest) {
            // 通知外界已经授予权限，
            mIPermission.granted();
            this.finish();
            return;
        }

        // 申请权限
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }

    /**
     * 权限申请回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean requestResult = PermissionUtils.requestPermissionSuccess(grantResults);
        if (requestResult) {
            // 通知外界已经申请成功
            mIPermission.granted();
            this.finish();
            return;
        }

        // 用户拒绝授权
        if (!PermissionUtils.shouldShowRequestPermissionRationale(this, permissions)) {
            mIPermission.denied();
            this.finish();
            return;
        }

        // 执行到这里，代表用户取消授权
        mIPermission.cancel();
        this.finish();
        return;
    }

    @Override
    public void finish() {
        super.finish();
        // 关闭动画效果
        overridePendingTransition(0, 0);
    }

    /**
     * 提供外界统一的权限申请入口
     * @param context
     * @param permissions 权限列表
     * @param requestCode 回调code
     * @param iPermission 回调函数
     */
    public static void requestPermissionAction(Context context,String[]permissions,int requestCode,IPermission iPermission){
        mIPermission = iPermission;
        Intent intent = new Intent(context,MyPermissionActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // 传递数据到当前Activity
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_REQUEST_CODE,requestCode);
        bundle.putStringArray(PARAM_PERMISSION,permissions);
        intent.putExtras(bundle);

        context.startActivity(intent);
    }
}