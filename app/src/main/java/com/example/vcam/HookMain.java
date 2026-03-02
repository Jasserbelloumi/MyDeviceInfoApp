package com.example.vcam;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class HookMain implements IXposedHookLoadPackage {
    private static XSharedPreferences prefs;

    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals("com.example.vcam")) return;

        // تحميل الإعدادات من التطبيق الرئيسي
        prefs = new XSharedPreferences("com.example.vcam", "vcam_settings");
        prefs.makeWorldReadable();

        XposedHelpers.findAndHookMethod("android.hardware.Camera", lpparam.classLoader, "setDisplayOrientation", int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                prefs.reload();
                if (prefs.getBoolean("enabled", true)) {
                    int userRotation = prefs.getInt("rotation", 0);
                    param.args[0] = userRotation;
                    XposedBridge.log("VCAM Pro: Forced Rotation to " + userRotation);
                }
            }
        });
    }
}
