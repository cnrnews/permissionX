package com.netease.permission.annotation;

import com.netease.permission.MyPermissionActivity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author lihl
 * @Date 2022/3/3 14:18
 * @Email 1601796593@qq.com
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {
    // 权限列表
    String[] value();

    // 回调code
    int requestCode() default MyPermissionActivity.PARAM_REQUEST_CODE_DEFAULT;
}
