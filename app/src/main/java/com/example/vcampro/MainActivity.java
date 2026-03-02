package com.example.vcampro;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        Button btnRotate = new Button(this);
        btnRotate.setText("تدوير الفيديو 90 درجة");
        btnRotate.setOnClickListener(v -> {
            // هنا سنضع كود حفظ الإعدادات ليقرأها الـ Hook
            Toast.makeText(this, "تم تدوير الفيديو", Toast.LENGTH_SHORT).show();
        });

        Button btnFlip = new Button(this);
        btnFlip.setText("قلب الفيديو (يمين/يسار)");
        
        layout.addView(btnRotate);
        layout.addView(btnFlip);
        setContentView(layout);
    }
}
