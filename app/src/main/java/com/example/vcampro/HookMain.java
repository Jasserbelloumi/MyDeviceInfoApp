package com.example.vcampro;

import android.hardware.Camera;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import java.util.List;

public class HookMain implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals("com.example.vcampro")) return;

        // منع التشوه عبر إجبار التطبيق على أبعاد محددة
        XposedHelpers.findAndHookMethod("android.hardware.Camera", lpparam.classLoader, "setParameters", Camera.Parameters.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Camera.Parameters params = (Camera.Parameters) param.args[0];
                // هنا نثبت الأبعاد لمنع الـ Stretching
                params.setPreviewSize(1280, 720);
                XposedBridge.log("VCAM Pro: Fixed Preview Size to 1280x720 for " + lpparam.packageName);
            }
        });
    }
}
