package com.example.vcampro;

import android.hardware.Camera;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import java.util.ArrayList;
import java.util.List;

public class HookMain implements IXposedHookLoadPackage {
    
    // متغيرات التحكم التي ستضاف لاحقاً للواجهة
    boolean is_mirror = false;
    int rotation_angle = 0; 

    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals("com.example.vcampro")) return;

        // 1. منع التشوه: إجبار الكاميرا على أحجام فيديو قياسية (16:9)
        XposedHelpers.findAndHookMethod("android.hardware.Camera$Parameters", lpparam.classLoader, "getSupportedPreviewSizes", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                List<Camera.Size> sizes = new ArrayList<>();
                Camera.Parameters params = (Camera.Parameters) param.thisObject;
                // إرجاع حجم ثابت يطابق معظم ملفات الفيديو لمنع التمدد
                sizes.add(params.new Size(1280, 720));
                sizes.add(params.new Size(640, 480));
                param.setResult(sizes);
            }
        });

        // 2. التحكم في الدوران (Rotation)
        XposedHelpers.findAndHookMethod("android.hardware.Camera", lpparam.classLoader, "setDisplayOrientation", int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                // هنا يمكننا التلاعب بالدوران الذي يطلبه التطبيق
                XposedBridge.log("VCAM Pro: App requested rotation: " + param.args[0]);
            }
        });
    }
}
