package android.ucam.edu.seiries;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.ucam.edu.seiries.FragmentListaSeries.SeriesListener;
import android.ucam.edu.seiries.beans.Serie;

public class FragmentSeriesMain extends AppCompatActivity implements SeriesListener{
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_series_main);
        FragmentListaSeries frgListado=(FragmentListaSeries) getSupportFragmentManager().findFragmentById(R.id.FrgListado);
        frgListado.setSeriesListener(this);
    }


    public void onSerieSeleccionada(Serie c) {
        boolean hayDetalle =(getSupportFragmentManager().findFragmentById(R.id.FrgDetalle) != null);

        if(hayDetalle) {
            if(c.getImageId()==-1){
                ((FragmentDetalle)getSupportFragmentManager().findFragmentById(R.id.FrgDetalle)).mostrarDetalle(c.getId(),c.getName(),c.getDescription(),c.getImageId(),c.getImageUri());
            }
            else{
                ((FragmentDetalle)getSupportFragmentManager().findFragmentById(R.id.FrgDetalle)).mostrarDetalle(c.getId(),c.getName(),c.getDescription(),c.getImageId(),null);
            }
        }
        else {
            Intent i = new Intent(FragmentSeriesMain.this, DetalleActivity.class);
            i.putExtra(DetalleActivity.EXTRA_ID, c.getId());
            i.putExtra(DetalleActivity.EXTRA_TITLE, c.getName());
            i.putExtra(DetalleActivity.EXTRA_TEXTO, c.getDescription());
            i.putExtra(DetalleActivity.EXTRA_IMG,c.getImageId());
            if(c.getImageId()==-1){
                i.putExtra(DetalleActivity.EXTRA_IMG_URI,c.getImageUri().toString());
            }
            else{
                i.putExtra(DetalleActivity.EXTRA_IMG_URI,"uri");
            }
            startActivity(i);
        }
    }

}
