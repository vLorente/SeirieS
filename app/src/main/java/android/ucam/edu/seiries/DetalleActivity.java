package android.ucam.edu.seiries;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class DetalleActivity extends AppCompatActivity {

    public static final String EXTRA_ID ="android.ucam.edu.semana3.EXTRA_ID";
    public static final String EXTRA_TEXTO ="android.ucam.edu.semana3.EXTRA_TEXTO";
    public static final String EXTRA_TITLE ="android.ucam.edu.semana3.EXTRA_TITLE";
    public static final String EXTRA_IMG ="android.ucam.edu.semana3.EXTRA_IMG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serie_detalle);

        FragmentDetalle detalle =(FragmentDetalle)getSupportFragmentManager().findFragmentById(R.id.FrgDetalle);
        detalle.mostrarDetalle(getIntent().getIntExtra(EXTRA_ID,-1),getIntent().getStringExtra(EXTRA_TITLE),getIntent().getStringExtra(EXTRA_TEXTO), getIntent().getStringExtra(EXTRA_IMG));
    }
}
