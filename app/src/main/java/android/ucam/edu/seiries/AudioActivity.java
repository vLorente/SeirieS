package android.ucam.edu.seiries;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;


public class AudioActivity extends AppCompatActivity {
    private MediaPlayer song;
    private ImageButton btn_play;
    private ImageButton btn_pause;
    private ImageButton btn_stop;
    private TextView title_song;
    private TextView artist_song;
    private boolean flag;
    private SeekBar mSeekBar;

    private Handler mHandler = new Handler();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_activity_layout);

        song = MediaPlayer.create(this, R.raw.gaspanic);
        btn_pause = findViewById(R.id.btn_pause);
        btn_play = findViewById(R.id.btn_play);
        btn_stop = findViewById(R.id.btn_stop);
        title_song = findViewById(R.id.txt_song_title);
        artist_song = findViewById(R.id.txt_artist_song);
        mSeekBar = findViewById(R.id.mSeekBar);
        flag = false;

        song.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                flag=true;
            }
        });

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag) song.start();
            }
        });

        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag) song.pause();
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag){
                    song.pause();
                    song.seekTo(0);
                }
            }
        });

        //Asiganar a la SeekBar el tamaño de la canción
        mSeekBar.setMax(song.getDuration()/1000);

        //Hacer que se actualice la SeekBar
        AudioActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(song != null){
                    int mCurrentPosition = song.getCurrentPosition() / 1000;
                    mSeekBar.setProgress(mCurrentPosition);
                }
                mHandler.postDelayed(this, 1000);
            }
        });

        //Listener para poner la parte de la cancion que seleccionemos en la SeekBat
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(song != null && fromUser){
                    song.seekTo(progress * 1000);
                }
            }
        });



        //Conseguir Título y Artista de los Metadatos de la canción mediante su uri
//        Uri songUri = Uri.parse("android.resource://"+getPackageName()+"/raw/gaspanic");
//        String scheme = songUri.getScheme();
//        String title="";
//        String artist="";
//
//        if(scheme.equals("content")) {
//            String[] proj = {MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST};
//            Cursor cursor = this.getContentResolver().query(songUri, proj, null, null, null);
//            if(cursor != null && cursor.getCount() > 0) {
//                cursor.moveToFirst();
//                if(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE) != -1) {
//                    title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID));
//                    artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
//                }
//            }
//        }
//
//        Log.d("METADATOS CANCION","Titulo: "+title);
//        Log.d("METADATOS CANCION","Artista: "+artist);

    }

}
