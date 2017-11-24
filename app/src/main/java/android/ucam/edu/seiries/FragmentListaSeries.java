package android.ucam.edu.seiries;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.ucam.edu.seiries.beans.Serie;
import android.ucam.edu.seiries.db.SeriesDB;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class FragmentListaSeries extends Fragment {

    private Resources res;
    private ArrayList<Serie> datos;
    private SeriesDB db;

    private ListView lstListado;
    private AdaptadorSeries adaptadorSeries;

    private SeriesListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lista_series, container, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        res = getResources();

        db = new SeriesDB(getContext());

        datos = db.getAllSeries();

        lstListado = getView().findViewById(R.id.lstSeries) ;

        adaptadorSeries = new AdaptadorSeries(this);

        lstListado.setAdapter(adaptadorSeries);

        lstListado.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> list, View view, int pos, long id) {
                if (listener!=null) {
                    listener.onSerieSeleccionada((
                            (Serie)lstListado.getAdapter().getItem(pos)));
                }
            }
        });



        final FloatingActionButton btn_add = (FloatingActionButton) getView().findViewById(R.id.floatingActionButton);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),ActivitiAddSerie.class);
                startActivity(intent);
            }
        });

        db.close();
    }

    

    class AdaptadorSeries extends ArrayAdapter<Serie> {

        Activity context;

        AdaptadorSeries(Fragment context) {
            super(context.getActivity(), R.layout.mylistview, datos);
            this.context = context.getActivity();
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView= inflater.inflate(R.layout.mylistview, null, true);
            TextView txtTitle = rowView.findViewById(R.id.txt);
            txtTitle.setText(datos.get(position).getName());

            try {
                ImageView imageView = rowView.findViewById(R.id.img);
                imageView.setImageResource(datos.get(position).getImageId());
            }
            catch (Exception e){
                ImageView imageView = rowView.findViewById(R.id.img);
                imageView.setImageURI(datos.get(position).getImageUri());
            }


            return(rowView);
        }
    }

    public interface SeriesListener {
        void onSerieSeleccionada(Serie c);
    }

    public void setSeriesListener(SeriesListener listener) {
        this.listener=listener;
    }



}
