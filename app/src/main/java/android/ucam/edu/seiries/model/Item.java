package android.ucam.edu.seiries.model;


import android.ucam.edu.seiries.beans.SerieBean;

import java.util.ArrayList;

public class Item {
    private String text;
    private ArrayList<SerieBean> series;
    private boolean isExpandable;

    public Item(String text, boolean isExpandable, ArrayList<SerieBean> series) {
        this.text = text;
        this.isExpandable = isExpandable;
        this.series = series;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isExpandable() {
        return isExpandable;
    }

    public void setExpandable(boolean expandable) {
        isExpandable = expandable;
    }

    public ArrayList<SerieBean> getSeries() {
        return series;
    }

    public void setSeries(ArrayList<SerieBean> series) {
        this.series = series;
    }
}
