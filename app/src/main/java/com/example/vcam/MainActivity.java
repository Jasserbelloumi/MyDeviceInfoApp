package com.example.vcam;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

public class MainActivity extends Activity {
    private static final int PICK_VIDEO = 1;
    private SharedPreferences prefs;
    private TextView pathText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // طلب الأذونات فوراً لمنع الانهيار
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }

        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(50, 50, 50, 50);

        // استخدام MODE_PRIVATE بدلاً من WORLD_READABLE لمنع الانهيار في أندرويد 10+
        prefs = getSharedPreferences("vcam_settings", Context.MODE_PRIVATE);
        
        pathText = new TextView(this);
        pathText.setTextSize(18);
        pathText.setText("Video Path: " + prefs.getString("video_path", "Not Selected"));

        Button btnPick = new Button(this);
        btnPick.setText("Select Video From Gallery");
        btnPick.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_VIDEO);
        });

        layout.addView(pathText);
        layout.addView(btnPick);
        setContentView(layout);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_VIDEO && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            String realPath = getRealPathFromURI(uri);
            if (realPath != null) {
                prefs.edit().putString("video_path", realPath).apply();
                pathText.setText("Selected: " + realPath);
                Toast.makeText(this, "Video Saved! Restart target app.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String path = null;
        String[] proj = { MediaStore.Video.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            path = cursor.getString(column_index);
            cursor.close();
        }
        return path;
    }
}
