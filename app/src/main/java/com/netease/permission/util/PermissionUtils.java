package com.netease.permission.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import com.netease.permission.menu.DefaultStartSettins;
import com.netease.permission.menu.IMenu;
import com.netease.permission.menu.OPPOStartSettings;
import com.netease.permission.menu.VIVOStartSettings;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import androidx.collection.SimpleArrayMap;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * @Author lihl
 * @Date 2022/3/3 13:50
 * @Email 1601796593@qq.com
 */
public class PermissionUtils {
    // 定义八种权限
    private static SimpleArrayMap<String, Integer> MIN_SDK_PERMISSIONS;

    static {
        MIN_SDK_PERMISSIONS = new SimpleArrayMap<>(8);
        MIN_SDK_PERMISSIONS.put("com.android.voicemail.permission.ADD_VOICEMAIL", 14);
        MIN_SDK_PERMISSIONS.put("android.permission.BODY_SENSORS", 20);
        MIN_SDK_PERMISSIONS.put("android.permission.READ_CALL_LOG", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.READ_EXTERNAL_STORAGE", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.USE_SIP", 9);
        MIN_SDK_PERMISSIONS.put("android.permission.WRITE_CALL_LOG", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.SYSTEM_ALERT_WINDOW", 23);
        MIN_SDK_PERMISSIONS.put("android.permission.WRITE_SETTINGS", 23);
    }


    private static HashMap<String, Class<? extends IMenu>> permissionMenu = new HashMap<>();

    private static final String MANUFACTURER_DEFAULT = "Default";//默认

    public static final String MANUFACTURER_HUAWEI = "huawei";//华为
    public static final String MANUFACTURER_MEIZU = "meizu";//魅族
    public static final String MANUFACTURER_XIAOMI = "xiaomi";//小米
    public static final String MANUFACTURER_SONY = "sony";//索尼
    public static final String MANUFACTURER_OPPO = "oppo";
    public static final String MANUFACTURER_LG = "lg";
    public static final String MANUFACTURER_VIVO = "vivo";
    public static final String MANUFACTURER_SAMSUNG = "samsung";//三星
    public static final String MANUFACTURER_LETV = "letv";//乐视
    public static final String MANUFACTURER_ZTE = "zte";//中兴
    public static final String MANUFACTURER_YULONG = "yulong";//酷派
    public static final String MANUFACTURER_LENOVO = "lenovo";//联想

    static {
        permissionMenu.put(MANUFACTURER_DEFAULT, DefaultStartSettins.class);
        permissionMenu.put(MANUFACTURER_OPPO, OPPOStartSettings.class);
        permissionMenu.put(MANUFACTURER_VIVO, VIVOStartSettings.class);
    }

    /**
     * 是否已经申请权限
     *
     * @param context
     * @param permissions
     * @return 返回false 代表需要申请权限，返回true代表不需要申请权限
     */
    public static boolean hasPermissionRequest(Context context, String... permissions) {
        for (String permission : permissions) {
            if (permissionExists(permission) && isPermissionReqeust(context, permission) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * 指定权限是否已经被授权了
     *
     * @param context
     * @param permission
     * @return
     */
    private static boolean isPermissionReqeust(Context context, String permission) {
        try {
            int checkPermissionResult = ContextCompat.checkSelfPermission(context, permission);
            return checkPermissionResult == PackageManager.PERMISSION_GRANTED;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查权限是否申请成功
     *
     * @param grantedResult
     * @return 返回true代表申请成功，返回false代表没有申请成功
     */
    public static boolean requestPermissionSuccess(int... grantedResult) {
        if (grantedResult == null || grantedResult.length <= 0) return false;
        for (int permissionValue : grantedResult) {
            if (permissionValue != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    // TODO 说白了：就是用户被拒绝过一次，然后又弹出这个框，【需要给用户一个解释，为什么要授权，就需要执行此方法判断】
    // 当用户点击了不再提示，这种情况要考虑到才行
    public static boolean shouldShowRequestPermissionRationale(Activity activity, String... permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 利用反射调用 object 中被 annotationClass 注解的方法
     *
     * @param object 代表 MainActivity
     * @param annotationClass
     */
    public static void invokeAnnotation(Object object, Class annotationClass) {
        Class<?> objectClass = object.getClass();

        Method[] declaredMethods = objectClass.getDeclaredMethods();

        for (Method declaredMethod : declaredMethods) {
            declaredMethod.setAccessible(true);

            // 方法是否有被 annotationClass 注解
            boolean annotationPresent = declaredMethod.isAnnotationPresent(annotationClass);
            if (annotationPresent) {
                try {
                    // 调用被注解的方法
                    declaredMethod.invoke(object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 检查指定权限是否存在
     *
     * @param permission
     * @return
     */
    private static boolean permissionExists(String permission) {
        Integer minVersion = MIN_SDK_PERMISSIONS.get(permission);
        return minVersion == null || minVersion <= Build.VERSION.SDK_INT;
    }

    /**
     * 跳转到系统设置页面
     *
     * @param context
     */
    public static void startAndroidSettings(Context context) {

        Class<? extends IMenu> aClass = permissionMenu.get(Build.MANUFACTURER.toLowerCase());
        if (aClass == null) {
            aClass = permissionMenu.get(MANUFACTURER_DEFAULT);
        }

        try {
            // 获取对应的系统设置类
            IMenu iMenu = aClass.newInstance();
            Intent startActivity = iMenu.getStartActivity(context);
            if (startActivity != null) {
                context.startActivity(startActivity);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}



