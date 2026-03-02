package com.example.vcampro;

import android.hardware.Camera;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class HookMain implements IXposedHookLoadPackage {
    
    // قيمة الدوران (يمكن تغييرها لاحقاً من التطبيق)
    // 0 = عادي، 90 = عمودي، 180 = مقلوب، 270 = عمودي عكسي
    int mRotation = 0; 

    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals("com.example.vcampro")) return;

        // اعتراض طلب التطبيق لتدوير الكاميرا وإجباره على القيمة التي نريدها
        XposedHelpers.findAndHookMethod("android.hardware.Camera", lpparam.classLoader, "setDisplayOrientation", int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                // إجبار التطبيق على استخدام الدوران الخاص بنا
                param.args[0] = mRotation;
                XposedBridge.log("VCAM Pro: Orientation forced to " + mRotation);
            }
        });
    }
}
