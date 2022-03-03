package com.netease.permission.aspect;

import android.content.Context;

import com.netease.permission.MyPermissionActivity;
import com.netease.permission.annotation.Permission;
import com.netease.permission.annotation.PermissionCancel;
import com.netease.permission.annotation.PermissionDenied;
import com.netease.permission.core.IPermission;
import com.netease.permission.util.PermissionUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import androidx.fragment.app.Fragment;

/**
 * @Author lihl
 * @Date 2022/3/3 14:28
 * @Email 1601796593@qq.com
 */
@Aspect
public class PermissionAspect {

    // 切入点：匹配程序中所有被@Permission注解标记的方法
    @Pointcut("execution(@com.netease.permission.annotation.Permission * *(..)) && @annotation(permission)")
    public void pointActionMethod(Permission permission) {
    }

    // 替换这个方法并且执行这个方法。在读写操作前替换成这个方法去执行权限申请的操作
    @Around("pointActionMethod(permission)")
    public void proceedingJoinPoint(final ProceedingJoinPoint point, Permission permission) throws Throwable {
        Context context = null;

        final Object thisObj = point.getThis();

        if (thisObj instanceof Context) {
            context = (Context) thisObj;
        } else if (thisObj instanceof Fragment) {
            context = ((Fragment) thisObj).getContext();
        }

        if (null == null || permission == null) {
            throw new IllegalAccessException("null == null || permission == null");
        }

        final Context finalContext = context;
        // 调用权限处理
        MyPermissionActivity.requestPermissionAction(context, permission.value(),
                permission.requestCode(), new IPermission() {
                    @Override
                    public void granted() { // 申请成功
                        try {
                            // 让程序继续执行下去
                            point.proceed();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }

                    @Override
                    public void cancel() { // 取消授权
                        // 执行 thisObj 中被PermissionCancel注解的方法
                        PermissionUtils.invokeAnnotation(thisObj, PermissionCancel.class);
                    }

                    @Override
                    public void denied() { // 拒绝授权
                        // 执行 thisObj 中被 @PermissionDenied 注解的方法
                        PermissionUtils.invokeAnnotation(thisObj, PermissionDenied.class);


                        // 告知用户，并跳转到手机设置界面
                        PermissionUtils.startAndroidSettings(finalContext);
                    }
                });

    }
}
