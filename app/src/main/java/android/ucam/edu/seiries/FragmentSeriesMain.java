package android.ucam.edu.seiries;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.ucam.edu.seiries.FragmentListaSeries.SeriesListener;
import android.ucam.edu.seiries.beans.SerieBean;

public class FragmentSeriesMain extends AppCompatActivity implements SeriesListener{
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_series_main);
        FragmentListaSeries frgListado=(FragmentListaSeries) getSupportFragmentManager().findFragmentById(R.id.FrgListado);
        frgListado.setSeriesListener(this);
    }


    public void onSerieSeleccionada(SerieBean c) {
        boolean hayDetalle =(getSupportFragmentManager().findFragmentById(R.id.FrgDetalle) != null);

        if(hayDetalle) {
            ((FragmentDetalle)getSupportFragmentManager().findFragmentById(R.id.FrgDetalle)).mostrarDetalle(c.getId(),c.getName(),c.getDescription(),c.getImageUriString(),c.getNum_capitulos(),c.getEvento(),c.getEstadoSerie(),c.getDia_salida());
        }
        else {
            Intent i = new Intent(FragmentSeriesMain.this, DetalleActivity.class);
            i.putExtra(DetalleActivity.EXTRA_ID, c.getId());
            i.putExtra(DetalleActivity.EXTRA_TITLE, c.getName());
            i.putExtra(DetalleActivity.EXTRA_TEXTO, c.getDescription());
            i.putExtra(DetalleActivity.EXTRA_IMG,c.getImageUriString());
            i.putExtra(DetalleActivity.EXTRA_CAPS,c.getNum_capitulos());
            i.putExtra(DetalleActivity.EXTRA_EVENTO,c.getEvento());
            i.putExtra(DetalleActivity.EXTRA_ESTADO,c.getEstadoSerie());
            i.putExtra(DetalleActivity.EXTRA_DIA,c.getDia_salida());
            startActivity(i);
        }
    }

}
