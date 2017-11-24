package android.ucam.edu.seiries.singletons;


import android.ucam.edu.seiries.beans.User;
import android.util.Log;
import java.util.HashMap;

public class MyProperties {
    private static MyProperties mInstance= null;

    private Boolean flag_log=false;
    private Boolean flag_cond=false;
    private HashMap<String,User> usuarios = new HashMap<>();
    private User admin = new User("admin","admin");



    protected MyProperties(){

        usuarios.put("admin",admin);


        Log.wtf("INFO","Generando datos del singleton...");

    }

    public static synchronized MyProperties getInstance(){
        if(null == mInstance){
            mInstance = new MyProperties();
        }
        return mInstance;
    }


    public void setUsuarios(HashMap<String, User> usuarios) {
        this.usuarios = usuarios;
    }

    public HashMap<String, User> getUsuarios() {

        return usuarios;
    }


    public void setFlag_log(Boolean flag_log) {
        this.flag_log = flag_log;
    }

    public Boolean getFlag_log() {

        return flag_log;
    }



    public void setFlag_cond(Boolean flag_cond) {
        this.flag_cond = flag_cond;
    }

    public Boolean getFlag_cond() {

        return flag_cond;
    }

//    public ArrayList<Serie> getSeries() {
//        return series;
//    }
//
//    public void addSerieImg(String name, @Nullable Integer image, String description,int dia,int estado) {
//        if(image==0){
//            Serie serie = new Serie(name,description,R.drawable.imgdefault,dia,EstadoSerie.DEFAULT.getById(estado));
//            this.series.add(serie);
//        }
//        else{
//            Serie serie = new Serie(name,description,image,dia,EstadoSerie.DEFAULT.getById(estado));
//            this.series.add(serie);
//        }
//    }
//
//    public void addSerieUri(String name, @Nullable Uri imageUri, String description,int dia,int estado) {
//            Serie serie = new Serie(name,description,imageUri,dia,EstadoSerie.DEFAULT.getById(estado));
//            this.series.add(serie);
//    }

}
