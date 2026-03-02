package com.example.vcam;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class HookMain implements IXposedHookLoadPackage {
    private static XSharedPreferences prefs;

    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        // لا نحقن الكود في تطبيقنا نفسه
        if (lpparam.packageName.equals("com.example.vcam")) return;

        prefs = new XSharedPreferences("com.example.vcam", "vcam_settings");
        prefs.makeWorldReadable();

        // 1. التقاط المسارات عند فتح الكاميرا
        XposedHelpers.findAndHookMethod("android.hardware.Camera", lpparam.classLoader, "open", int.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("VCAM Pro: Detected Camera Open for package: " + lpparam.packageName);
            }
        });

        // 2. حقن الفيديو في الـ SurfaceTexture (المسؤول عن العرض)
        XposedHelpers.findAndHookMethod("android.hardware.Camera", lpparam.classLoader, "setPreviewTexture", 
            SurfaceTexture.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                prefs.reload();
                String videoPath = prefs.getString("video_path", "/sdcard/DCIM/Camera1/virtual.mp4");
                SurfaceTexture st = (SurfaceTexture) param.args[0];
                
                if (st != null) {
                    XposedBridge.log("VCAM Pro: Injecting video from path: " + videoPath);
                    VideoPlayer.play(st, videoPath);
                }
            }
        });
    }
}
