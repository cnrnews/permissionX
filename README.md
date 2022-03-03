### permissionX
Android动态权限申请,可以同时申请多个权限，并对申请结果进行通知回调
 
### 采用技术
aspectj ： 面向切面编程，实现权限申请

注解 ： 标注需要申请的权限列表

反射 :  利用反射获取注解信息，对权限申请结果进行回调

依赖倒置：面向接口而不是面向细节编程（针对不同机型跳转不同的手机系统设置）

### 使用说明

```java
// 注解需要申请的权限
    @Permission(value = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, requestCode = 200)
    public void permissionRequest() {
        Toast.makeText(this, "申请成功", Toast.LENGTH_SHORT).show();
    }

    // 用户取消授权时会回调到这里
    @PermissionCancel
    public void permissionCancel() {
        Toast.makeText(this, "权限被取消", Toast.LENGTH_SHORT).show();
    }

    // 用户拒绝权限时会回调到这里
    @PermissionDenied
    public void permissionDenied() {
        Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
    }
```
	 
### 参考文章
https://blog.csdn.net/zxy_de_android/article/details/106752310
