package android.ucam.edu.seiries;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;


public class VideoActivity extends AppCompatActivity{

    private Button btn_youtube;
    private Button btn_play;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_activity_layout);

        btn_youtube = findViewById(R.id.btn_youtube);
        btn_play = findViewById(R.id.btn_play_video);

        btn_youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VideoActivity.this,YoutubeVideoActivity.class));
            }
        });

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideo();
            }
        });

    }

    private void playVideo() {
        VideoView videoView = (VideoView) findViewById(R.id.videoView);
        MediaController mc = new MediaController(this);
        mc.setAnchorView(videoView);
        mc.setMediaPlayer(videoView);
        videoView.setMediaController(mc);
        videoView.setVideoPath("android.resource://android.ucam.edu.seiries/raw/ucam");
        videoView.requestFocus();
        videoView.start();
    }

}
