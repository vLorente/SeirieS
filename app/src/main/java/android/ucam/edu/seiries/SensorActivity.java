package android.ucam.edu.seiries;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class SensorActivity extends AppCompatActivity {

    private Button btn_sensors;
    private Button btn_game1;
    private Button btn_proximity;
    private ArrayList<String> sensorsList;
    private ImageButton starwarsBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_activity);

        //inicializacion y variables
        btn_sensors = findViewById(R.id.btn_sensores);
        btn_game1 = findViewById(R.id.btn_game);
        btn_proximity = findViewById(R.id.btn_proximity);
        starwarsBtn = findViewById(R.id.starwarsBtn);
        sensorsList = new ArrayList<>();
        //fin inicializacion y variables

        btn_sensors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SensorManager sensorManager = (SensorManager)
                        getSystemService(SENSOR_SERVICE);
                List<Sensor> listaSensores =sensorManager.getSensorList(Sensor.TYPE_ALL);
                Log.d("SENSOR", "/////LISTADO SENSORES/////");
                for(Sensor sensor: listaSensores) {
                    Log.d("SENSOR ",sensor.getName());
                    sensorsList.add(sensor.getName());
                }

                //Dialog para mostrar al usuario los sensores disponibles
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(SensorActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.dialog_list, null);
                alertDialog.setView(convertView);
                alertDialog.setTitle(getString(R.string.list_sensor));
                ListView lv = (ListView) convertView.findViewById(R.id.list_dialog);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SensorActivity.this,android.R.layout.simple_list_item_1,sensorsList);
                lv.setAdapter(adapter);
                alertDialog.setNeutralButton(R.string.ok,null);
                alertDialog.show();
            }
        });

        btn_game1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SensorActivity.this,GameAcelerActivity.class));
            }
        });

        btn_proximity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SensorActivity.this,ProximitySensorActivity.class));
            }
        });

        starwarsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LaserSaberActivity.class));
            }
        });

    }
}
