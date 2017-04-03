package com.style.lib.media.video;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.VideoView;


public class PlayVideoActivity extends AppCompatActivity {

    private String path;

    private VideoView videoView;
    private TextView textExist;
    private View container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//没有标题
        setContentView(R.layout.activity_play_video);

        path = getIntent().getStringExtra("path");


        videoView = (VideoView) findViewById(R.id.videoview);

        textExist = (TextView) findViewById(R.id.tv_exist);

        container = findViewById(R.id.rl_container);

        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        playVideo();
    }


    private void playVideo() {


        videoView.setVideoPath(path);
        // 开始播放
        videoView.start();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                textExist.setVisibility(View.VISIBLE);
                videoView.start();
            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {

                return false;
            }
        });
    }
}
