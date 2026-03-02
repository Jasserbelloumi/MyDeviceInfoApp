package com.example.vcampro;

import android.hardware.Camera;
import android.graphics.SurfaceTexture;
import android.view.Surface;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class HookMain implements IXposedHookLoadPackage {
    
    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        // لا نقوم بعمل هوك لتطبيقنا نحن
        if (lpparam.packageName.equals("com.example.vcampro")) return;

        XposedBridge.log("VCAM Pro: Hooking package: " + lpparam.packageName);

        // اعتراض فتح الكاميرا (Camera 1 API)
        XposedHelpers.findAndHookMethod("android.hardware.Camera", lpparam.classLoader, "setPreviewTexture", 
            SurfaceTexture.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                // هنا سنقوم لاحقاً بإدخال مشغل الفيديو المعدل
                // وسنضيف كود التحكم في (Matrix) لتدوير الفيديو يمين/يسار
                XposedBridge.log("VCAM Pro: Camera Preview Hooked!");
            }
        });
    }
}
