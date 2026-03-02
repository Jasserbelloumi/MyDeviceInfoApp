package com.example.vcam;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final int PICK_VIDEO = 1;
    private SharedPreferences prefs;
    private TextView pathText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // واجهة برمجية سريعة
        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(50, 50, 50, 50);

        pathText = new TextView(this);
        prefs = getSharedPreferences("vcam_settings", Context.MODE_WORLD_READABLE);
        pathText.setText("Current Video: " + prefs.getString("video_path", "None"));

        Button btnPick = new Button(this);
        btnPick.setText("Select Video From Gallery");
        btnPick.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_VIDEO);
        });

        layout.addView(pathText);
        layout.addView(btnPick);
        setContentView(layout);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_VIDEO && resultCode == RESULT_OK && data != null) {
            Uri selectedVideo = data.getData();
            String realPath = selectedVideo.toString(); // في التطبيق الحقيقي سنحتاج محول مسارات
            prefs.edit().putString("video_path", realPath).apply();
            pathText.setText("Selected: " + realPath);
            Toast.makeText(this, "Video Saved! Restart target app.", Toast.LENGTH_LONG).show();
        }
    }
}
