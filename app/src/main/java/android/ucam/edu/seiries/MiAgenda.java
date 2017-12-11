package android.ucam.edu.seiries;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.ucam.edu.seiries.adapter.MyRecycleAdapter;
import android.ucam.edu.seiries.beans.SerieBean;
import android.ucam.edu.seiries.model.Item;
import android.util.Log;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MiAgenda extends AppCompatActivity {


    private static final String SERIES_ID = "series";
    private static final String TAG = "MI AGENDA";
    //private ArrayList<SerieBean> datos = new ArrayList<>();
    private DatabaseReference dbRef;
    private DatabaseReference seriesRef;
    private RecyclerView lista;
    private RecyclerView.LayoutManager layoutManager;
    private List<Item> items = new ArrayList<>();
    private LottieAnimationView animacion;
    private MyRecycleAdapter adapter;
    final ArrayList<SerieBean> lunes = new ArrayList<>();
    final ArrayList<SerieBean> martes = new ArrayList<>();
    final ArrayList<SerieBean> miercoles = new ArrayList<>();
    final ArrayList<SerieBean> jueves = new ArrayList<>();
    final ArrayList<SerieBean> viernes = new ArrayList<>();
    final ArrayList<SerieBean> sabado = new ArrayList<>();
    final ArrayList<SerieBean> domingo = new ArrayList<>();
    final List<ArrayList<SerieBean>> arraysDias = new ArrayList<>();
    private FloatingActionButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_agenda);




        dbRef = FirebaseDatabase.getInstance().getReference();
        seriesRef = dbRef.child(SERIES_ID);
        animacion = findViewById(R.id.animationAgenda);
        lista = (RecyclerView) findViewById(R.id.miRecycler);
        lista.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        lista.setLayoutManager(layoutManager);
        activarAnimation();
        setData();
        esperarYCargarDatos(3000);

    }

    private void setData() {


        seriesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if(dataSnapshot.getValue(SerieBean.class).getEstadoSerie() == 0){
                    switch (dataSnapshot.getValue(SerieBean.class).getDia_salida()){
                        case 0:
                            lunes.add(dataSnapshot.getValue(SerieBean.class));
                            break;
                        case 1:
                            martes.add(dataSnapshot.getValue(SerieBean.class));
                            break;
                        case 2:
                            miercoles.add(dataSnapshot.getValue(SerieBean.class));
                            break;
                        case 3:
                            jueves.add(dataSnapshot.getValue(SerieBean.class));
                            break;
                        case 4:
                            viernes.add(dataSnapshot.getValue(SerieBean.class));
                            break;
                        case 5:
                            sabado.add(dataSnapshot.getValue(SerieBean.class));
                            break;
                        case 6:
                            domingo.add(dataSnapshot.getValue(SerieBean.class));
                            break;
                        default:
                            break;
                    }
                }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void esperarYCargarDatos(int milisegundos) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                arraysDias.add(lunes);
                arraysDias.add(martes);
                arraysDias.add(miercoles);
                arraysDias.add(jueves);
                arraysDias.add(viernes);
                arraysDias.add(sabado);
                arraysDias.add(domingo);

                Resources res = getResources();
                String[] dias = res.getStringArray(R.array.days);
                for (int i=0; i<dias.length;i++){
                    if(arraysDias.get(i).size()>0){
                        Item item = new Item(dias[i],true,arraysDias.get(i));
                        items.add(item);
                    } else {
                        Item item = new Item(dias[i],false,arraysDias.get(i));
                        items.add(item);
                    }


                }

                Log.e(TAG, "Tamaño array "+lunes.size());

                adapter = new MyRecycleAdapter(items);
                lista.setAdapter(adapter);

                desactivarAnimation();
            }
        }, milisegundos);
    }

    public void activarAnimation(){
        animacion.playAnimation();
        animacion.setVisibility(View.VISIBLE);
        lista.setVisibility(View.GONE);
    }

    public void desactivarAnimation(){
        animacion.setVisibility(View.GONE);
        lista.setVisibility(View.VISIBLE);
        animacion.pauseAnimation();
    }
}
