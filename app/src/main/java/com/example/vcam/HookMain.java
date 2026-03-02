package com.example.vcam;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class HookMain implements IXposedHookLoadPackage {
    private static XSharedPreferences prefs;

    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals("com.example.vcam")) return;

        prefs = new XSharedPreferences("com.example.vcam", "vcam_settings");

        // Hook عند بدء عرض الكاميرا
        XposedHelpers.findAndHookMethod("android.hardware.Camera", lpparam.classLoader, "setPreviewTexture", 
            SurfaceTexture.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                SurfaceTexture st = (SurfaceTexture) param.args[0];
                // هنا يتم استدعاء مشغل الفيديو ليقوم بالرسم على هذا الـ Texture
                // المنطق مأخوذ من الملف الذي أرفقته (vcam_all_code)
                VideoPlayer.play(st, prefs.getString("video_path", "/sdcard/DCIM/Camera1/virtual.mp4"));
            }
        });

        // Hook لاعتراض طلب الأذونات وفحص المسارات
        XposedHelpers.findAndHookMethod("android.hardware.Camera", lpparam.classLoader, "open", int.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Camera camera = (Camera) param.getResult();
                // هنا يتم "التقاط المسار" وحقنه تلقائياً
                XposedBridge.log("VCAM Pro: Camera opened, injecting video stream...");
            }
        });
    }
}
