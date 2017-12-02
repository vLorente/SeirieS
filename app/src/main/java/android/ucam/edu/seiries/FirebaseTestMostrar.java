package android.ucam.edu.seiries;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.ucam.edu.seiries.beans.SerieBean;
import android.ucam.edu.seiries.customs.CustomAdapter;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FirebaseTestMostrar extends AppCompatActivity {

    ListView lista;
    DatabaseReference dbRef;
    DatabaseReference seriesRef;
    ArrayList<SerieBean> series = new ArrayList<>();
    CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_test_mostrar);

        lista = findViewById(R.id.listView);
        dbRef = FirebaseDatabase.getInstance().getReference();
        seriesRef = dbRef.child("series");
        adapter = new CustomAdapter(this,series);
        lista.setAdapter(adapter);

        seriesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e("TEST MOSTRAR", "Se añade una serie al array");
                series.add(dataSnapshot.getValue(SerieBean.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                series.remove(dataSnapshot.getValue(SerieBean.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
