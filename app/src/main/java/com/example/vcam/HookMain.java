package com.example.vcam;

import android.graphics.SurfaceTexture;
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
        if (lpparam.packageName.equals("com.example.vcam")) return;

        // تحميل الإعدادات من حزمة التطبيق
        prefs = new XSharedPreferences("com.example.vcam", "vcam_settings");
        prefs.makeWorldReadable(); // محاولة جعلها قابلة للقراءة من قِبل Xposed

        XposedHelpers.findAndHookMethod("android.hardware.Camera", lpparam.classLoader, "setPreviewTexture", 
            SurfaceTexture.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                prefs.reload();
                String videoPath = prefs.getString("video_path", null);
                SurfaceTexture st = (SurfaceTexture) param.args[0];
                
                if (st != null && videoPath != null) {
                    XposedBridge.log("VCAM Pro: Injecting video into " + lpparam.packageName);
                    VideoPlayer.play(st, videoPath);
                }
            }
        });
    }
}
