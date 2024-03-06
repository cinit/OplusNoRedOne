package dev.tmpfs.oplus.redonekiller.hook;

import androidx.annotation.Keep;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import dev.tmpfs.oplus.redonekiller.util.Log;

@Keep
public class HookEntry implements IXposedHookLoadPackage, IXposedHookZygoteInit {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        // filter with target process name
        // Log.d("handleLoadPackage: process = " + lpparam.processName + ", package = " + lpparam.packageName +
        //        "isFirstApplication = " + lpparam.isFirstApplication);
        if (!"com.android.systemui".equals(lpparam.processName)) {
            return;
        }
        // the code is loaded by context.createPackageContext(packageName, 3).getClassLoader() for OxygenOS 14
        // where flags 3 stands for CONTEXT_INCLUDE_CODE | CONTEXT_IGNORE_SECURITY
        if (!"com.android.systemui".equals(lpparam.packageName) &&
                !"com.oplus.aod".equals(lpparam.packageName) &&
                !"com.oplus.uiengine".equals(lpparam.packageName)) {
            return;
        }
        try {
            DisableRedOneClock.init(lpparam.classLoader);
            HideAodSilentNotificationIcon.init(lpparam.classLoader);
        } catch (Exception | LinkageError | AssertionError e) {
            Log.e("handleLoadPackage", e);
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) {
        // no-op
    }

}
