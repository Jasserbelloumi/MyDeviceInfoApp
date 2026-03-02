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
import android.widget.LinearLayout;

public class MainActivity extends Activity {
    private static final int PICK_VIDEO = 1;
    private SharedPreferences prefs;
    private TextView pathText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(60, 60, 60, 60);

        // استخدمنا MODE_PRIVATE لإيقاف الانهيار
        prefs = getSharedPreferences("vcam_settings", Context.MODE_PRIVATE);
        
        pathText = new TextView(this);
        pathText.setText("Selected Video: " + prefs.getString("video_path", "None"));
        pathText.setPadding(0, 0, 0, 40);

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
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_VIDEO && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            String path = getPath(uri);
            if (path != null) {
                prefs.edit().putString("video_path", path).apply();
                pathText.setText("Selected: " + path);
                Toast.makeText(this, "Saved! Please restart target app.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getPath(Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }
}
