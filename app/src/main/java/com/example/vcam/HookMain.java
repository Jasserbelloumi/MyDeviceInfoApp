package com.example.vcam;
import android.graphics.SurfaceTexture;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class HookMain implements IXposedHookLoadPackage {
    public void handleLoadPackage(LoadPackageParam lpp) throws Throwable {
        if (lpp.packageName.equals("com.example.vcam")) return;
        
        final XSharedPreferences prefs = new XSharedPreferences("com.example.vcam", "vcam_settings");

        XposedHelpers.findAndHookMethod("android.hardware.Camera", lpp.classLoader, "setPreviewTexture", 
            SurfaceTexture.class, new XC_MethodHook() {
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                prefs.reload();
                String path = prefs.getString("video_path", null);
                if (path != null) {
                    VideoPlayer.play((SurfaceTexture)param.args[0], path);
                }
            }
        });
    }
}
