package android.ucam.edu.seiries.beans;

import android.net.Uri;


public class Serie {

    private int id;
    private String name;
    private String description;
    private int imageId;
    private Uri imageUri;
    private String imageUriString;
    private int num_capitulos;
    //Día que sale el capítulo
    private int dia_salida;
    //Estado de la serie
    private int estadoSerie;
    //Tiene evento?
    private int evento;


    public Serie (){}

    public Serie (String name, String description, int imageId, int dia_salida, int estadoSerie, int num_capitulos, int evento){
        this.name=name;
        this.description=description;
        this.imageId=imageId;
        this.dia_salida=dia_salida;
        this.estadoSerie=estadoSerie;
        this.num_capitulos=num_capitulos;
        this.evento=evento;
    }

    public Serie (String name, String description, Uri imageUri,int dia_salida,int estadoSerie, int num_capitulos,int evento){
        this.name=name;
        this.description=description;
        this.imageUri=imageUri;
        this.imageId=-1;
        this.dia_salida=dia_salida;
        this.estadoSerie=estadoSerie;
        this.num_capitulos=num_capitulos;
        this.evento=evento;
    }
    public Serie (String name, String description, String imageUriString,int dia_salida,int estadoSerie, int num_capitulos,int evento){
        this.name=name;
        this.description=description;
        this.imageUriString=imageUriString;
        this.imageId=-1;
        this.dia_salida=dia_salida;
        this.estadoSerie=estadoSerie;
        this.num_capitulos=num_capitulos;
        this.evento=evento;
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getImageId() {
        return imageId;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public int getEstadoSerie() {
        return estadoSerie;
    }

    public int getDia_salida() {
        return dia_salida;
    }

    public void setEstadoSerie(int estadoSerie) {
        this.estadoSerie = estadoSerie;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public void setDia_salida(int dia_salida) {
        this.dia_salida = dia_salida;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNum_capitulos() {
        return num_capitulos;
    }

    public void setNum_capitulos(int num_capitulos) {
        this.num_capitulos = num_capitulos;
    }

    public int getEvento() {
        return evento;
    }

    public void setEvento(int evento) {
        this.evento = evento;
    }
}
