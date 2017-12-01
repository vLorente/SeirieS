package android.ucam.edu.seiries;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;


public class FragmentDetalle extends Fragment {

    private long id;
    private static final String DEBUG_TAG = "FragmentDetalle";
    private URL url;
    private Uri uri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_series_detalle, container, false);
    }

    @Override
    public void onActivityCreated(@android.support.annotation.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void mostrarDetalle(final long id, String title, String texto, String imageUirDownload, int capitulos, int evento, int estado, int dia) {
        this.id=id;
        TextView txtTitle = (TextView)getView().findViewById(R.id.textTitle);
        txtTitle.setText(title);
        TextView txtDetalle = (TextView)getView().findViewById(R.id.textDetalle);
        txtDetalle.setText(texto);
        Resources res = getResources();
        TextView txtEstado = (TextView)getView().findViewById(R.id.txt_estado);
        String[] estados = res.getStringArray(R.array.estdos_array);
        txtEstado.setText(estados[estado]);
        TextView txtCapitulos = getView().findViewById(R.id.txt_capitulos);
        txtCapitulos.setText(""+capitulos);
        TextView txtEvento = getView().findViewById(R.id.txt_evento);
        String[] dias = res.getStringArray(R.array.days);
        if(evento==0){
            txtEvento.setText("No hay recordatorio");
        } else {
            txtEvento.setText("Recordatorio los "+dias[dia]);
        }
        ImageView imageDetalle = (ImageView) getView().findViewById(R.id.imgDetalle);

        try {
            url = new URL(imageUirDownload);
            uri = Uri.parse(url.toURI().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        Glide.with(getContext())
                .load(uri)
                .fitCenter()
                .centerCrop()
                .into(imageDetalle);

        FloatingActionButton btn_preferences = getView().findViewById(R.id.btn_info);
        btn_preferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),ActivityUpdateSerie.class);
                intent.putExtra("ID",id);
                startActivity(intent);
            }
        });

        //Insertamos un Dialog al intentar borrar para controlar que no se borre la serie por error
        //Cuando borramos una serie también se borra su evento en calendario en caso de que tenga
        FloatingActionButton btn_delete = getView().findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder altdial = new AlertDialog.Builder(getContext());
                altdial.setMessage("¿Seguro qué quieres eliminar esta serie?").setCancelable(false)
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //BORRRAR SERIE////

//                                SeriesDB db = new SeriesDB(getContext());
//                                Serie serie = db.getSerieById(id);
//                                db.deleteSerie(id);
//                                db.close();
//                                if(serie.getEvento()==1){
//                                    Log.wtf("Borrando Serie","Tiene evento la serie "+id+"? "+serie.getEvento());
//                                    EventosDB eventosDB = new EventosDB(getContext());
//                                    long eventID=eventosDB.deleteEvent(serie.getId());
//                                    eventosDB.close();
//                                    deleteEventById(eventID);
//                                }
                                Intent intent = new Intent(getContext(), FragmentSeriesMain.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = altdial.create();
                alert.setTitle("Eliminar");
                alert.show();
            }
        });

    }

    //Eliminar un evento del calendario
    public void deleteEventById (long eventID){

        Uri deleteUri = ContentUris.withAppendedId(Events.CONTENT_URI, eventID);
        int rows = getContext().getContentResolver().delete(deleteUri, null, null);
        Log.i(DEBUG_TAG, "Rows deleted: " + rows);
    }
}
