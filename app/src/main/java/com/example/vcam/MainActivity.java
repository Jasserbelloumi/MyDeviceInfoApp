package com.example.vcam;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences prefs = getSharedPreferences("vcam_settings", Context.MODE_WORLD_READABLE);
        final Switch swEnable = findViewById(R.id.switch_enable);
        final RadioGroup rgRot = findViewById(R.id.radioGroup_rotation);
        Button btnSave = findViewById(R.id.btn_save);

        // تحميل الإعدادات الحالية
        swEnable.setChecked(prefs.getBoolean("enabled", true));
        int currentRot = prefs.getInt("rotation", 0);
        if(currentRot == 90) rgRot.check(R.id.rot90);
        else if(currentRot == 180) rgRot.check(R.id.rot180);
        else if(currentRot == 270) rgRot.check(R.id.rot270);
        else rgRot.check(R.id.rot0);

        btnSave.setOnClickListener(v -> {
            int selectedRot = 0;
            int id = rgRot.getCheckedRadioButtonId();
            if(id == R.id.rot90) selectedRot = 90;
            else if(id == R.id.rot180) selectedRot = 180;
            else if(id == R.id.rot270) selectedRot = 270;

            prefs.edit()
                .putBoolean("enabled", swEnable.isChecked())
                .putInt("rotation", selectedRot)
                .apply();
            
            Toast.makeText(this, "Settings Saved! Restart target apps.", Toast.LENGTH_SHORT).show();
        });
    }
}
