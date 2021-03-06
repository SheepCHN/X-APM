package github.tornaco.xposedmoduletest.xposed;

import android.util.Log;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.xposedmoduletest.xposed.service.IModuleBridge;
import github.tornaco.xposedmoduletest.xposed.service.XAppGuardServiceDelegate;
import github.tornaco.xposedmoduletest.xposed.service.XIntentFirewallServiceDelegate;
import github.tornaco.xposedmoduletest.xposed.submodules.AppGuardSubModuleManager;
import github.tornaco.xposedmoduletest.xposed.submodules.IntentFirewallSubModuleManager;
import github.tornaco.xposedmoduletest.xposed.submodules.SubModule;
import github.tornaco.xposedmoduletest.xposed.util.XposedLog;

/**
 * Created by guohao4 on 2017/10/31.
 * Email: Tornaco@163.com
 */

class XModuleImplSeparable extends XModuleAbs {

    XModuleImplSeparable() {
        IModuleBridge appguard = new XAppGuardServiceDelegate();
        for (SubModule s : AppGuardSubModuleManager.getInstance().getAllSubModules()) {
            s.onBridgeCreate(appguard);
        }
        IModuleBridge firewall = new XIntentFirewallServiceDelegate();
        for (SubModule s : IntentFirewallSubModuleManager.getInstance().getAllSubModules()) {
            s.onBridgeCreate(firewall);
        }
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedLog.verbose("handleLoadPackage: " + lpparam.packageName);

        for (SubModule s : AppGuardSubModuleManager.getInstance().getAllSubModules()) {
            if (s.getInterestedPackages().contains(lpparam.packageName)
                    || s.getInterestedPackages().contains("*")) {
                try {
                    // XposedLog.wtf("Invoking submodule@handleLoadPackage: " + s.name());
                    s.handleLoadingPackage(lpparam.packageName, lpparam);
                } catch (Throwable e) {
                    XposedLog.wtf("Error call handleLoadingPackage submodule:" + s
                            + " , trace: " + Log.getStackTraceString(e));
                }
            }
        }

        for (SubModule s : IntentFirewallSubModuleManager.getInstance().getAllSubModules()) {
            if (s.getInterestedPackages().contains(lpparam.packageName)
                    || s.getInterestedPackages().contains("*")) {
                try {
                    // XposedLog.wtf("Invoking submodule@handleLoadPackage: " + s.name());
                    s.handleLoadingPackage(lpparam.packageName, lpparam);
                } catch (Throwable e) {
                    XposedLog.wtf("Error call handleLoadingPackage submodule:" + s
                            + " , trace: " + Log.getStackTraceString(e));
                }
            }
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        for (SubModule s : AppGuardSubModuleManager.getInstance().getAllSubModules()) {
            try {
                s.initZygote(startupParam);
            } catch (Throwable e) {
                XposedLog.wtf("Error call initZygote submodule:" + s
                        + " , trace: " + Log.getStackTraceString(e));
            }
        }

        for (SubModule s : IntentFirewallSubModuleManager.getInstance().getAllSubModules()) {
            try {
                s.initZygote(startupParam);
            } catch (Throwable e) {
                XposedLog.wtf("Error call initZygote submodule:" + s
                        + " , trace: " + Log.getStackTraceString(e));
            }
        }
    }
}
