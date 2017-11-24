package android.ucam.edu.seiries.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.ucam.edu.seiries.beans.Serie;
import android.util.Log;

import java.util.ArrayList;


public class SeriesDB extends SQLiteOpenHelper {

    private final static String NOMBRE_TABLE ="Series";
    private final static int DATABASE_VERSION = 7;
    private final static String KEY_ID ="id";
    private final static String KEY_NAME ="name";
    private final static String KEY_DESCRIPTION ="description";
    private final static String KEY_IMAGE_ID ="imageId";
    private final static String KEY_IMAGE_URI ="imageUri";
    private final static String KEY_DIA ="dia_salida";
    private final static String KEY_ESTADO ="estadoSerie";
    private final static String KEY_NUM_CAPS ="num_caps";
    private final static String KEY_EVENT= "event";



    String creacion = "CREATE TABLE IF NOT EXISTS "+NOMBRE_TABLE+" ("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_NAME+" TEXT, " +
            KEY_DESCRIPTION+" TEXT, "+KEY_IMAGE_URI+" TEXT,"+KEY_IMAGE_ID+" INTEGER, "+KEY_DIA+" INTEGER, "+KEY_ESTADO+" INTEGER, "+KEY_NUM_CAPS+" INTEGER, "+KEY_EVENT+" INTEGER)";

    public SeriesDB(Context context) {
        super(context, NOMBRE_TABLE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(creacion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionantigua, int versionnueva) {
        db.execSQL("DROP TABLE IF EXISTS "+NOMBRE_TABLE);
        db.execSQL(creacion);
    }

    //Añadir nueva serie
    public  void addSerie(Serie serie){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, serie.getName());
        values.put(KEY_DESCRIPTION, serie.getDescription());
        if(serie.getImageId()==-1){
            values.put(KEY_IMAGE_ID,serie.getImageId());
            values.put(KEY_IMAGE_URI,serie.getImageUri().toString());
        }else {
            values.put(KEY_IMAGE_ID,serie.getImageId());
        }
        values.put(KEY_DIA,serie.getDia_salida());
        values.put(KEY_ESTADO,serie.getEstadoSerie());
        values.put(KEY_NUM_CAPS,serie.getNum_capitulos());
        values.put(KEY_EVENT,serie.getEvento());

        db.insert(NOMBRE_TABLE,null,values);
        db.close();
    }


    //Actualizar nueva serie
    public  void updateSerie(int id,Serie serie){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, serie.getName());
        values.put(KEY_DESCRIPTION, serie.getDescription());
        if(serie.getImageId()==-1){
            values.put(KEY_IMAGE_ID,serie.getImageId());
            values.put(KEY_IMAGE_URI,serie.getImageUri().toString());
        }else {
            values.put(KEY_IMAGE_ID,serie.getImageId());
        }
        values.put(KEY_DIA,serie.getDia_salida());
        values.put(KEY_ESTADO,serie.getEstadoSerie());
        values.put(KEY_NUM_CAPS,serie.getNum_capitulos());
        values.put(KEY_EVENT,serie.getEvento());

        String where = KEY_ID+"= '"+id+"'";
        db.update(NOMBRE_TABLE,values,where,null);
        db.close();
    }

    //Buscar serie por el ID
    public Serie getSerieById(int id){
        Serie serie = new Serie();
        String selectQuery = "SELECT * FROM "+NOMBRE_TABLE+" WHERE "+KEY_ID+" = '"+id+"'";
        //Log.wtf("QUERY",selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        if(cursor != null){
            cursor.moveToFirst();
        }
        serie.setId(cursor.getInt(0));
        serie.setName(cursor.getString(1));
        serie.setDescription(cursor.getString(2));
        serie.setImageId(cursor.getInt(4));
        if(cursor.getInt(4)==-1){
            serie.setImageUri(Uri.parse(cursor.getString(3)));
        }
        serie.setDia_salida(cursor.getInt(5));
        serie.setEstadoSerie(cursor.getInt(6));
        serie.setNum_capitulos(cursor.getInt(7));
        serie.setEvento(cursor.getInt(8));
        db.close();
        return serie;
    }

    //Devolver el listado de series
    public ArrayList<Serie> getAllSeries(){
        ArrayList<Serie> series = new ArrayList<>();
        String selectQuery = "SELECT * FROM "+NOMBRE_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Recorremos las filas resultado y añadimos las series al Array
        if(cursor.moveToFirst()){
            do{
                Serie serie = new Serie();
                serie.setId(cursor.getInt(0));
                serie.setName(cursor.getString(1));
                serie.setDescription(cursor.getString(2));
                serie.setImageId(cursor.getInt(4));
                if(cursor.getInt(4)==-1){
                    serie.setImageUri(Uri.parse(cursor.getString(3)));
                }
                serie.setDia_salida(cursor.getInt(5));
                serie.setEstadoSerie(cursor.getInt(6));
                serie.setNum_capitulos(cursor.getInt(7));
                series.add(serie);
            } while (cursor.moveToNext());
        }
        db.close();
        return  series;
    }

    //Devuelve el cursor con las series
    public Cursor getCursorAllSeries(){
        ArrayList<Serie> series = new ArrayList<>();
        String selectQuery = "SELECT * FROM "+NOMBRE_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        db.close();
        return  cursor;
    }

    //Comprobar si la tabla contiene alguna fila
    public boolean isEmpty (){
        int count = 0;
        String selectQuery = "SELECT count(*) FROM "+NOMBRE_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        try {
            if(cursor != null)
                if(cursor.getCount() > 0){
                    cursor.moveToFirst();
                    count = cursor.getInt(0);
                }
        }finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        db.close();

        if(count>0)
            return false;
        else
            return true;
    }

    //Eliminar serie
    public void deleteSerie(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String where = KEY_ID+"= '"+id+"'";
        db.delete(NOMBRE_TABLE,where,null);
        db.close();
    }

    //Eliminar la tabla Series
    public void deleteDB(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+NOMBRE_TABLE);
        db.close();
    }

    //Id de la ultima serie añadid
    public int getLastSerieId(){
        String selectQuery = "SELECT MAX("+KEY_ID+") FROM "+NOMBRE_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor!=null){
            cursor.moveToFirst();
        }
        Log.wtf("Ultimo ID", "El último id es: "+cursor.getInt(0));
        return cursor.getInt(0);
    }

}
