package com.example.vcampro;

import android.hardware.Camera;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import java.util.List;

public class HookMain implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals("com.example.vcampro")) return;

        // منع تشوه الصورة عبر إجبار الكاميرا على أبعاد 16:9 أو 4:3 قياسية
        XposedHelpers.findAndHookMethod("android.hardware.Camera$Parameters", lpparam.classLoader, "setPreviewSize", int.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                // منع التطبيقات من طلب أبعاد غريبة تسبب التمدد
                int width = (int) param.args[0];
                int height = (int) param.args[1];
                
                // إذا كان الارتفاع أكبر من العرض (وضع عمودي)، نقوم بالتصحيح
                if (height > width) {
                    param.args[0] = 720;
                    param.args[1] = 1280;
                } else {
                    param.args[0] = 1280;
                    param.args[1] = 720;
                }
            }
        });
    }
}
