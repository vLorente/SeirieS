package android.ucam.edu.seiries;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.ucam.edu.seiries.beans.SerieBean;
import android.ucam.edu.seiries.customs.CustomAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class FragmentListaSeries extends Fragment {

    private static final String SERIES_ID ="series";
    private Resources res;
    private ArrayList<SerieBean> datos = new ArrayList<>();
    private DatabaseReference dbRef;
    private DatabaseReference seriesRef;
    private ListView lstListado;
    private CustomAdapter adaptadorSeries;

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

        dbRef = FirebaseDatabase.getInstance().getReference();
        seriesRef = dbRef.child(SERIES_ID);

        lstListado = getView().findViewById(R.id.lstSeries) ;

        adaptadorSeries = new CustomAdapter(getActivity(),datos);

        seriesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                datos.add(dataSnapshot.getValue(SerieBean.class));
                adaptadorSeries.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                datos.remove(dataSnapshot.getValue(SerieBean.class));
                adaptadorSeries.notifyDataSetChanged();
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



}
