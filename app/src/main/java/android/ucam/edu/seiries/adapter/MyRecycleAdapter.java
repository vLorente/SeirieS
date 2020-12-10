package android.ucam.edu.seiries.adapter;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.ucam.edu.seiries.R;
import android.ucam.edu.seiries.customs.CustomAdapter;
import android.ucam.edu.seiries.model.Item;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.Utils;

import java.util.List;

class MyViewHolderWithoutChild extends RecyclerView.ViewHolder{

    public TextView textView;

    public MyViewHolderWithoutChild(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.textPadre);
    }
}

class MyViewHolderWithChild extends RecyclerView.ViewHolder{

    public TextView textView;
    public RelativeLayout btnExpandir;
    public ExpandableLinearLayout expandableLayout;
    public ListView listView;

    public MyViewHolderWithChild(View itemView) {
        super(itemView);

        textView = itemView.findViewById(R.id.textPadre);
        btnExpandir = itemView.findViewById(R.id.buttonAgenda);
        expandableLayout = itemView.findViewById(R.id.expandableLayout);
        listView = itemView.findViewById(R.id.listViewAgenda);

    }
}

public class MyRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<Item> items;
    Context context;
    SparseBooleanArray expandState = new SparseBooleanArray();

    public MyRecycleAdapter(List<Item> items) {
        this.items = items;
        for (int i=0;i<items.size();i++){
            expandState.append(i,false);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if(items.get(position).isExpandable())
            return 1;
        else
            return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        if(viewType == 0){ //Sin Items
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.layout_without_child,parent,false);
            return new MyViewHolderWithoutChild(view);
        } else{
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.layout_with_child,parent,false);
            return new MyViewHolderWithChild(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        switch (holder.getItemViewType()){
            case 0:
            {
                MyViewHolderWithoutChild viewHolder = (MyViewHolderWithoutChild) holder;
                Item item = items.get(position);
                viewHolder.setIsRecyclable(false);
                viewHolder.textView.setText(item.getText());
            }
            break;
            case 1:
            {
                final MyViewHolderWithChild viewHolder = (MyViewHolderWithChild) holder;
                Item item = items.get(position);
                viewHolder.setIsRecyclable(false);
                viewHolder.textView.setText(item.getText());

                viewHolder.expandableLayout.setInRecyclerView(true);
                viewHolder.expandableLayout.setExpanded(expandState.get(position));
                viewHolder.expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {

                    @Override
                    public void onPreOpen() {
                        changeRotate(viewHolder.btnExpandir,0f,180f).start();
                        expandState.put(position,true);
                    }

                    @Override
                    public void onPreClose() {
                        changeRotate(viewHolder.btnExpandir,180f,0f).start();
                        expandState.put(position,false);
                    }


                });

                viewHolder.btnExpandir.setRotation(expandState.get(position)?180f:0f);
                viewHolder.btnExpandir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewHolder.expandableLayout.toggle();
                    }
                });

                CustomAdapter adapter = new CustomAdapter(context,item.getSeries());

                viewHolder.listView.setAdapter(adapter);


            }
            break;
            default:
                break;
        }
    }

    private ObjectAnimator changeRotate(RelativeLayout btnExpandir, float to, float from) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(btnExpandir,"rotation",from,to);
        objectAnimator.setDuration(300);
        objectAnimator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return objectAnimator;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
