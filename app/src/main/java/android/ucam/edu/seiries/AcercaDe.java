package android.ucam.edu.seiries;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class AcercaDe extends AppCompatActivity {

    private InputStream ficheroraw;
    private BufferedReader br;
    private TextView etiqueta;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acercade);

        etiqueta = findViewById(R.id.acercade);
        try
        {
            String cadena="";
            ficheroraw = getResources().openRawResource(R.raw.acercade);
            br =new BufferedReader(new InputStreamReader(ficheroraw));
            while ((cadena = br.readLine()) != null){
                Log.i("Aplicacion Ficheros raw", cadena);
                etiqueta.setText(cadena);
            }
            ficheroraw.close();
        }
        catch (IOException e){
            e.printStackTrace();}
    }

}
