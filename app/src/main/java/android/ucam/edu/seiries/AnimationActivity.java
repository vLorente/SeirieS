package android.ucam.edu.seiries;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;


public class AnimationActivity extends AppCompatActivity {

    private ImageView rick1;
    private ImageView rick2;
    private Button play_animation;
    private MediaPlayer sound;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation_activity_layout);

        rick1 = findViewById(R.id.img_rick1);
        rick2 = findViewById(R.id.img_rick2);
        play_animation = findViewById(R.id.btn_play_animation);
        final Animation animation1 = AnimationUtils.loadAnimation(this,R.anim.animation1);
        final Animation animation2 = AnimationUtils.loadAnimation(this,R.anim.animation2);
        sound = MediaPlayer.create(this, R.raw.pickle_rick);

        //lanzamos el evento
        play_animation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Reproducir sonido
                sound.start();
                //Aplicar las animaciones
                rick2.startAnimation(animation2);
                rick1.startAnimation(animation1);
            }
        });

    }
}
