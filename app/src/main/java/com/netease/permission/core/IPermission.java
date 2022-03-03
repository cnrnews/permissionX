package com.netease.permission.core;

/**
 * @Author lihl
 * @Date 2022/3/3 13:42
 * @Email 1601796593@qq.com
 * <p>
 * 权限申请结果
 */
public interface IPermission {
    void granted(); // 已经授权

    void cancel(); // 取消授权

    void denied(); // 拒绝授权
}
