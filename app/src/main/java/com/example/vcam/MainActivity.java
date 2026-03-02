package com.example.vcam;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
        
        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(50, 50, 50, 50);

        prefs = getSharedPreferences("vcam_settings", Context.MODE_WORLD_READABLE);
        pathText = new TextView(this);
        pathText.setText("Current Video: " + prefs.getString("video_path", "Default"));

        Button btnPick = new Button(this);
        btnPick.setText("Choose Video From Gallery");
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
        if (requestCode == PICK_VIDEO && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            String realPath = getRealPathFromURI(uri);
            if (realPath != null) {
                prefs.edit().putString("video_path", realPath).apply();
                pathText.setText("Selected: " + realPath);
                Toast.makeText(this, "Success! Restart your camera app.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Video.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }
}
