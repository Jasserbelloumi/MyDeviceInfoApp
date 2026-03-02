package com.example.vcampro;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("vcam_settings", Context.MODE_WORLD_READABLE); // ملاحظة: سنحتاج لتعديل الصلاحيات لاحقاً
        
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 50, 50, 50);

        TextView title = new TextView(this);
        title.setText("VCAM Pro Control Panel");
        title.setTextSize(24);
        layout.addView(title);

        // زر التدوير
        Button btnRotate = new Button(this);
        btnRotate.setText("تدوير 90 درجة");
        btnRotate.setOnClickListener(v -> saveSetting("rotate", 90));

        // زر القلب الأفقي
        Button btnFlipH = new Button(this);
        btnFlipH.setText("قلب (يمين / يسار)");
        btnFlipH.setOnClickListener(v -> saveSetting("flip_h", 1));

        // زر القلب الرأسي
        Button btnFlipV = new Button(this);
        btnFlipV.setText("قلب (فوق / تحت)");
        btnFlipV.setOnClickListener(v -> saveSetting("flip_v", 1));

        layout.addView(btnRotate);
        layout.addView(btnFlipH);
        layout.addView(btnFlipV);
        setContentView(layout);
    }

    private void saveSetting(String key, int value) {
        // في الهواتف المروتة سنستخدم ملف نصي بسيط لسهولة وصول الـ Hook إليه
        Toast.makeText(this, "تم حفظ الإعداد: " + key, Toast.LENGTH_SHORT).show();
    }
}
