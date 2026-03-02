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
    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals("com.example.vcampro")) return;

        // اعتراض قائمة الأحجام المدعومة لمنع التشوه
        XposedHelpers.findAndHookMethod("android.hardware.Camera", lpparam.classLoader, "getSupportedPreviewSizes", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                // إجبار التطبيق على رؤية حجم واحد فقط وهو حجم الفيديو الخاص بنا
                List<Camera.Size> sizes = new ArrayList<>();
                Camera.Parameters params = (Camera.Parameters) param.thisObject;
                Camera.Size customSize = params.new Size(1280, 720);
                sizes.add(customSize);
                param.setResult(sizes);
                XposedBridge.log("VCAM Pro: Forced Preview Size to 1280x720 to prevent distortion");
            }
        });
    }
}
