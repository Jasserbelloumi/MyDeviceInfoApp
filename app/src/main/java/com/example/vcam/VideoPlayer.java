package com.example.vcam;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.view.Surface;

public class VideoPlayer {
    private static MediaPlayer mp;
    public static void play(SurfaceTexture st, String path) {
        try {
            if (mp != null) { mp.release(); }
            mp = new MediaPlayer();
            mp.setSurface(new Surface(st));
            mp.setDataSource(path);
            mp.setLooping(true);
            mp.prepare();
            mp.start();
        } catch (Exception e) {}
    }
}
