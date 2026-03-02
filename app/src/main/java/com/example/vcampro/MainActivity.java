package com.example.vcampro;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("VCAM Pro: Active\nGo to Settings to Configure Rotation");
        setContentView(tv);
    }
}
