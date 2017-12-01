package android.ucam.edu.seiries;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class DetalleActivity extends AppCompatActivity {

    public static final String EXTRA_ID ="android.ucam.edu.semana3.EXTRA_ID";
    public static final String EXTRA_TEXTO ="android.ucam.edu.semana3.EXTRA_TEXTO";
    public static final String EXTRA_TITLE ="android.ucam.edu.semana3.EXTRA_TITLE";
    public static final String EXTRA_IMG ="android.ucam.edu.semana3.EXTRA_IMG";
    public static final String EXTRA_CAPS = "android.ucam.edu.semana3.EXTRA_CAPS";
    public static final String EXTRA_EVENTO = "android.ucam.edu.semana3.EXTRA_EVENTO";
    public static final String EXTRA_ESTADO = "android.ucam.edu.semana3.EXTRA_ESTADO";
    public static final String EXTRA_DIA = "android.ucam.edu.semana3.EXTRA_DIA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serie_detalle);

        FragmentDetalle detalle =(FragmentDetalle)getSupportFragmentManager().findFragmentById(R.id.FrgDetalle);
        detalle.mostrarDetalle(getIntent().getIntExtra(EXTRA_ID,-1),getIntent().getStringExtra(EXTRA_TITLE),getIntent().getStringExtra(EXTRA_TEXTO), getIntent().getStringExtra(EXTRA_IMG),getIntent().getIntExtra(EXTRA_CAPS,0),getIntent().getIntExtra(EXTRA_EVENTO,0),getIntent().getIntExtra(EXTRA_ESTADO,0),getIntent().getIntExtra(EXTRA_DIA,0));
    }
}
