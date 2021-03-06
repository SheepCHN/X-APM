package github.tornaco.xposedmoduletest.xposed.service;

import android.util.Log;

import github.tornaco.xposedmoduletest.xposed.util.XposedLog;
import lombok.AllArgsConstructor;

/**
 * Created by guohao4 on 2017/12/25.
 * Email: Tornaco@163.com
 */
@AllArgsConstructor
public class ErrorCatchRunnable implements Runnable {

    private Runnable action;
    private String actionName;

    @Override
    public void run() {
        try {
            action.run();
        } catch (Throwable e) {
            XposedLog.wtf("Error@" + actionName + "\n" + Log.getStackTraceString(e));
        }
    }
}
