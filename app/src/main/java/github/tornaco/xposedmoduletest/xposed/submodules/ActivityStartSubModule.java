package github.tornaco.xposedmoduletest.xposed.submodules;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.xposedmoduletest.xposed.app.XAppGuardManager;
import github.tornaco.xposedmoduletest.xposed.app.XAppVerifyMode;
import github.tornaco.xposedmoduletest.xposed.service.VerifyListener;
import github.tornaco.xposedmoduletest.xposed.util.XLog;

/**
 * Created by guohao4 on 2017/10/31.
 * Email: Tornaco@163.com
 */

class ActivityStartSubModule extends AppGuardAndroidSubModule {
    @Override
    public void handleLoadingPackage(String pkg, XC_LoadPackage.LoadPackageParam lpparam) {
        hookStartActivityMayWait(lpparam);
    }

    private void hookStartActivityMayWait(XC_LoadPackage.LoadPackageParam lpparam) {
        XLog.logV("hookStartActivityMayWait...");
        try {
            Class clz = clzForStartActivityMayWait(lpparam);

            // Search method.
            String targetMethodName = "startActivityMayWait";
            int matchCount = 0;
            int activityOptsIndex = -1;
            int intentIndex = -1;
            Method startActivityMayWaitMethod = null;
            if (clz != null) {
                for (Method m : clz.getDeclaredMethods()) {
                    if (m.getName().equals(targetMethodName)) {
                        startActivityMayWaitMethod = m;
                        startActivityMayWaitMethod.setAccessible(true);
                        matchCount++;

                        Class[] classes = m.getParameterTypes();
                        for (int i = 0; i < classes.length; i++) {
                            if (Bundle.class == classes[i]) {
                                activityOptsIndex = i;
                            } else if (Intent.class == classes[i]) {
                                intentIndex = i;
                            }
                        }

                        if (activityOptsIndex >= 0 && intentIndex >= 0) {
                            break;
                        }
                    }
                }
            }

            if (startActivityMayWaitMethod == null) {
                XLog.logF("*** FATAL can not find startActivityMayWait method ***");
                setStatus(SubModuleStatus.ERROR);
                setErrorMessage("*** FATAL can not find startActivityMayWait method ***");
                return;
            }

            if (matchCount > 1) {
                XLog.logF("*** FATAL more than 1 startActivityMayWait method ***");
                setStatus(SubModuleStatus.ERROR);
                setErrorMessage("*** FATAL more than 1 startActivityMayWait method ***");
                // return;
            }

            if (intentIndex < 0) {
                XLog.logF("*** FATAL can not find intentIndex ***");
                setStatus(SubModuleStatus.ERROR);
                setErrorMessage("*** FATAL can not find intentIndex ***");
                return;
            }

            XLog.logF("startActivityMayWait method:" + startActivityMayWaitMethod);
            XLog.logF("intentIndex index:" + intentIndex);
            XLog.logF("activityOptsIndex index:" + activityOptsIndex);

            final int finalActivityOptsIndex = activityOptsIndex;
            final int finalIntentIndex = intentIndex;

            final Method finalStartActivityMayWaitMethod = startActivityMayWaitMethod;
            XC_MethodHook.Unhook unhook = XposedBridge.hookMethod(startActivityMayWaitMethod, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(final MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    try {
                        Intent intent =
                                finalIntentIndex > 0 ?
                                        (Intent) param.args[finalIntentIndex]
                                        : null;
                        if (intent == null) return;

                        ComponentName componentName = intent.getComponent();
                        if (componentName == null) return;
                        final String pkgName = componentName.getPackageName();

                        boolean isHomeIntent = isLauncherIntent(intent);
                        if (isHomeIntent) {
                            return;
                        }

                        // Package has been passed.
                        if (!getAppGuardBridge().onEarlyVerifyConfirm(pkgName)) {
                            return;
                        }

                        Bundle options =
                                finalActivityOptsIndex > 0 ?
                                        (Bundle) param.args[finalActivityOptsIndex]
                                        : null;

                        getAppGuardBridge().verify(options, pkgName, 0, 0,
                                new VerifyListener() {
                                    @Override
                                    public void onVerifyRes(String pkg, int uid, int pid, int res) {
                                        if (res == XAppVerifyMode.MODE_ALLOWED) try {
                                            XposedBridge.invokeOriginalMethod(finalStartActivityMayWaitMethod, param.thisObject, param.args);
                                        } catch (Exception e) {
                                            XLog.logF("Error@" + Log.getStackTraceString(e));
                                        }
                                    }
                                });
                        param.setResult(ActivityManager.START_SUCCESS);
                    } catch (Throwable e) {
                        // replacing did not work.. but no reason to crash the VM! Log the error and go on.
                        XLog.logF("Error@startActivityMayWaitMethod:" + Log.getStackTraceString(e));
                    } finally {
                    }
                }
            });
            XLog.logF("hookStartActivityMayWait OK: " + unhook);
            getBridge().publishFeature(XAppGuardManager.Feature.START);
            setStatus(unhookToStatus(unhook));
        } catch (Exception e) {
            XLog.logF("Fail hookStartActivityMayWait:" + e);
            setStatus(SubModuleStatus.ERROR);
            setErrorMessage(Log.getStackTraceString(e));
        }
    }

    Class clzForStartActivityMayWait(XC_LoadPackage.LoadPackageParam lpparam) throws ClassNotFoundException {
        throw new IllegalStateException("Need impl here");
    }
}