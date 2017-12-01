package android.ucam.edu.seiries;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.ucam.edu.seiries.adapter.MyRecycleAdapter;
import android.ucam.edu.seiries.model.Item;

import java.util.ArrayList;
import java.util.List;

public class MiAgenda extends AppCompatActivity {

    private RecyclerView lista;
    private RecyclerView.LayoutManager layoutManager;
    List<Item> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_agenda);

        lista = (RecyclerView) findViewById(R.id.miRecycler);
        lista.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        lista.setLayoutManager(layoutManager);

        setData();
    }

    private void setData() {
        for (int i=0; i<20;i++){
            if(i%2==0){
                Item item = new Item("Este es el item "+(i+1),"Este es el hijo "+(i+1),true);
                items.add(item);
            } else {
                Item item = new Item("Este es el item "+(i+1),"",false);
                items.add(item);
            }
        }

        MyRecycleAdapter adapter = new MyRecycleAdapter(items);
        lista.setAdapter(adapter);
    }
}
