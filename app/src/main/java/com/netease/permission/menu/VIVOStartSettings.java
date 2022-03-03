package com.netease.permission.menu;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

/**
 * @Author lihl
 * @Date 2022/3/3 14:51
 * @Email 1601796593@qq.com
 */
public class VIVOStartSettings implements IMenu {
    @Override
    public Intent getStartActivity(Context context) {

        Intent appIntent = context.getPackageManager().getLaunchIntentForPackage("coom.iqoo.secure");
        if (appIntent != null && Build.VERSION.SDK_INT < 23) {
            context.startActivity(appIntent);
        }

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Settings.ACTION_SETTINGS);
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        return intent;
    }
}
