package android.ucam.edu.seiries;

import android.content.Context;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

public class LaserSaberActivity extends AppCompatActivity implements SensorEventListener{

    private Switch bando;
    private ConstraintLayout layout;
    private Resources res;
    private ImageView laser;
    private FloatingActionButton btnOnOff;
    private MediaPlayer sound;
    private MediaPlayer sound2;
    private SensorManager sensorManager;
    private boolean flag = false;
    private static final String TAG = "LASER";
    private static final float MIN_PITCH = -0.7f;
    private static final float MAX_PITCH = 0.7f;
    private final float[] mAccelerometerReading = new float[3];
    private final float[] mMagnetometerReading = new float[3];

    private final float[] mRotationMatrix = new float[9];
    private final float[] mOrientationAngles = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laser_saber);
        bando = findViewById(R.id.elegir_bando);
        layout = findViewById(R.id.saberLayout);
        laser = findViewById(R.id.laser);
        res = getResources();
        layout.setBackgroundColor(res.getColor(R.color.jediBackground));
        btnOnOff = findViewById(R.id.btn_on_off);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sound2 = MediaPlayer.create(getApplicationContext(),R.raw.lightsaber_mover);

        bando.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    compoundButton.setText(R.string.sith);
                    layout.setBackgroundColor(res.getColor(R.color.sithBackground));
                    laser.setImageResource(R.drawable.redlaser);
                } else {
                    compoundButton.setText(R.string.cabjedi);
                    layout.setBackgroundColor(res.getColor(R.color.jediBackground));
                    laser.setImageResource(R.drawable.greenlaser);
                }
            }
        });

        btnOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(laser.getVisibility() == View.INVISIBLE){
                    sound = MediaPlayer.create(getApplicationContext(), R.raw.lightsaber_on);
                    sound.start();
                    laser.setVisibility(View.VISIBLE);
                    bando.setEnabled(false);
                    flag = true;
                } else {
                    sound = MediaPlayer.create(getApplicationContext(), R.raw.lightsaber_off);
                    sound.start();
                    laser.setVisibility(View.INVISIBLE);
                    bando.setEnabled(true);
                    flag = false;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)) {
            System.arraycopy(event.values, 0, mAccelerometerReading,
                    0, mAccelerometerReading.length);
        }
        else if (event.sensor == sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)) {
            System.arraycopy(event.values, 0, mMagnetometerReading,
                    0, mMagnetometerReading.length);
        }
        updateOrientationAngles();
        Log.e("ORIENTATION ANGLES","0: "+mOrientationAngles[0]+" 1: "+mOrientationAngles[1]+" 2: "+mOrientationAngles[2]);
    }

    // Compute the three orientation angles based on the most recent readings from
    // the device's accelerometer and magnetometer.
    public void updateOrientationAngles() {
        // Update rotation matrix, which is needed to update orientation angles.
        sensorManager.getRotationMatrix(mRotationMatrix, null,
                mAccelerometerReading, mMagnetometerReading);

        // "mRotationMatrix" now has up-to-date information.
        sensorManager.getOrientation(mRotationMatrix, mOrientationAngles);

        if(mOrientationAngles[1]<MIN_PITCH || mOrientationAngles[1]>MAX_PITCH){
            Log.e(TAG,"Entra en la condicion");
            if(flag){
                sonidoMovimiento(0);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void sonidoMovimiento(int milisegundos) {
        Log.e(TAG,"Entra al hilo");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                sound2.start();
            }
        }, milisegundos);
    }
}
