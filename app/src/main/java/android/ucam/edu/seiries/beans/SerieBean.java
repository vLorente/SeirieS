package android.ucam.edu.seiries.beans;

public class SerieBean {
    private String name;
    private String description;
    private String imageUriString;
    private int num_capitulos;
    //Día que sale el capítulo
    private int dia_salida;
    //Estado de la serie
    private int estadoSerie;
    //Tiene evento?
    private int evento;


    public SerieBean (){}

    public SerieBean (String name, String description, String imageUriString, int dia_salida, int estadoSerie, int num_capitulos, int evento){
        this.name=name;
        this.description=description;
        this.imageUriString=imageUriString;
        this.dia_salida=dia_salida;
        this.estadoSerie=estadoSerie;
        this.num_capitulos=num_capitulos;
        this.evento=evento;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUriString() {
        return imageUriString;
    }

    public void setImageUriString(String imageUriString) {
        this.imageUriString = imageUriString;
    }

    public int getNum_capitulos() {
        return num_capitulos;
    }

    public void setNum_capitulos(int num_capitulos) {
        this.num_capitulos = num_capitulos;
    }

    public int getDia_salida() {
        return dia_salida;
    }

    public void setDia_salida(int dia_salida) {
        this.dia_salida = dia_salida;
    }

    public int getEstadoSerie() {
        return estadoSerie;
    }

    public void setEstadoSerie(int estadoSerie) {
        this.estadoSerie = estadoSerie;
    }

    public int getEvento() {
        return evento;
    }

    public void setEvento(int evento) {
        this.evento = evento;
    }
}
