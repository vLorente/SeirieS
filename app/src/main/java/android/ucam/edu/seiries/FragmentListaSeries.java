package android.ucam.edu.seiries;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.ucam.edu.seiries.beans.SerieBean;
import android.ucam.edu.seiries.customs.CustomAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class FragmentListaSeries extends Fragment {

    private static final String SERIES_ID ="series";
    private static final String TAG ="FRAGMENT LISTA SERIES";
    private ArrayList<SerieBean> datos = new ArrayList<>();
    private DatabaseReference dbRef;
    private DatabaseReference seriesRef;
    private ListView lstListado;
    private CustomAdapter adaptadorSeries;
    private LottieAnimationView animacion;

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


        animacion = getView().findViewById(R.id.animationLoadSeries);
        dbRef = FirebaseDatabase.getInstance().getReference();
        seriesRef = dbRef.child(SERIES_ID);
        lstListado = getView().findViewById(R.id.lstSeries) ;

        adaptadorSeries = new CustomAdapter(getActivity(),datos);


        seriesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                activarAnimation();
                datos.add(dataSnapshot.getValue(SerieBean.class));
                adaptadorSeries.notifyDataSetChanged();
                desactivarAnimation();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                activarAnimation();
                long id = dataSnapshot.getValue(SerieBean.class).getId();
                Log.e(TAG, "El id de la serie es "+id);
                for (SerieBean serie : datos){
                    if(serie.getId() == id){
                        datos.remove(serie);
                        break;
                    }
                }
                datos.add(dataSnapshot.getValue(SerieBean.class));
                adaptadorSeries.notifyDataSetChanged();
                desactivarAnimation();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                activarAnimation();
                datos.remove(dataSnapshot.getValue(SerieBean.class));
                adaptadorSeries.notifyDataSetChanged();
                desactivarAnimation();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        lstListado.setAdapter(adaptadorSeries);

        lstListado.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> list, View view, int pos, long id) {
                if (listener!=null) {
                    listener.onSerieSeleccionada((
                            (SerieBean)lstListado.getAdapter().getItem(pos)));
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
    }


    public interface SeriesListener {
        void onSerieSeleccionada(SerieBean c);
    }

    public void setSeriesListener(SeriesListener listener) {
        this.listener=listener;
    }


    public void activarAnimation(){
        animacion.setVisibility(View.VISIBLE);
        lstListado.setVisibility(View.GONE);
    }

    public void desactivarAnimation(){
        animacion.setVisibility(View.GONE);
        lstListado.setVisibility(View.VISIBLE);
    }

}
