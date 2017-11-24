package android.ucam.edu.seiries;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class MultimediaActivity extends AppCompatActivity {

    private Button btn_audio;
    private Button btn_video;
    private Button btn_animation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multimedia_activity);

        //inicializacion
        btn_audio = findViewById(R.id.btn_audio);
        btn_video = findViewById(R.id.btn_video);
        btn_animation = findViewById(R.id.btn_animation);
        //fin inicializacion

        btn_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MultimediaActivity.this, AudioActivity.class));
            }
        });

        btn_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MultimediaActivity.this,VideoActivity.class));
            }
        });

        btn_animation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MultimediaActivity.this, AnimationActivity.class));
            }
        });
    }
}
