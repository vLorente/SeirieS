package android.ucam.edu.seiries.customs;

import android.app.Activity;
import android.net.Uri;
import android.ucam.edu.seiries.R;
import android.ucam.edu.seiries.beans.SerieBean;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;


public class CustomAdapter extends ArrayAdapter<SerieBean> {

    private URL url;
    private Uri uri;
    private final Activity context;
    private ArrayList<SerieBean> series;
    public CustomAdapter(Activity context,ArrayList<SerieBean> series) {
        super(context, android.R.layout.simple_list_item_1,series);
        this.context = context;
        this.series = series;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.mylistview, null, true);
        TextView txtTitle = rowView.findViewById(R.id.txt);

        ImageView imageView = rowView.findViewById(R.id.img);
        txtTitle.setText(series.get(position).getName());



        try {
            url = new URL(series.get(position).getImageUriString());
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
                .into(imageView);

        return rowView;
    }
}


