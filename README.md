# 注意!

>Src来自X-APM，Fork一下，防止源码失传😂😂

# 介绍

## 1. 目的
> 借助XPOSED实现系统级别应用管理。

### 1.1 功能

**应用锁**

* 应用启动验证。
* 最近任务验证。
* 最近任务高斯模糊（算法来自fastblur）。
* 自定义的验证器。
* 闯入抓拍。
* 指纹验证。

**自启动管理**

* 限制某些（用户选择）应用的开机广播接收。

**管理启动管理**

* 限制某些（用户选择）应用的关联启动（服务/广播）。

**锁屏清理**

* 屏幕锁定后清理后台应用（用户可设置白名单）。

**应用组件控制（开发中）**

* 禁用某应用的某些（用户选择）组件（服务/广播）。

## 2. 功能演示
参考README最下方demo视频。

## 3. 设计思路
系统服务Hook与Binder服务注入。

核心服务：
https://github.com/SheepCHN/X-APM/blob/master/app/src/main/java/github/tornaco/xposedmoduletest/xposed/service/XAshmanServiceImpl.java
https://github.com/SheepCHN/X-APM/blob/master/app/src/main/java/github/tornaco/xposedmoduletest/xposed/service/XAppGuardServiceImpl.java

Xposed模块代理：
https://github.com/SheepCHN/X-APM/blob/master/app/src/main/java/github/tornaco/xposedmoduletest/xposed/XModuleDelegate.java

## 4. 编译
依赖hiddenapi，Xposed-Framework。

## 4.1 自选编译
https://github.com/SheepCHN/X-APM/tree/master/build_var_controls

## 5. 测试
5.1 [查看最新测试报告](TestResults-XAppGuardManagerTest.html)

5.2 测试代码位于```androidTest```目录下。
