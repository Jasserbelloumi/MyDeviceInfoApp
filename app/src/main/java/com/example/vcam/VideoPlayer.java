package com.example.vcam;

import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.view.Surface;
import java.io.IOException;

public class VideoPlayer {
    private static MediaPlayer mediaPlayer;

    public static void play(SurfaceTexture surfaceTexture, String videoPath) {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
            mediaPlayer = new MediaPlayer();
            Surface surface = new Surface(surfaceTexture);
            mediaPlayer.setSurface(surface);
            mediaPlayer.setDataSource(videoPath);
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
