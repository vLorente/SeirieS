package android.ucam.edu.seiries.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventosDB extends SQLiteOpenHelper {

    private static final String NOMBRE_TABLA = "MisEventos";
    private static int DATABASE_VERSION = 1;
    private static final String KEY_ID ="id";
    private static final String KEY_ID_SERIE ="id_serie";
    private static final String KEY_ID_CALENDAR ="id_calendar";

    String creacion = "CREATE TABLE IF NOT EXISTS "+NOMBRE_TABLA+" ("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_ID_SERIE+" INTEGER, "+KEY_ID_CALENDAR+" INTEGER)";

    public EventosDB(Context context) {
        super(context, NOMBRE_TABLA, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(creacion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionantigua, int versionnueva) {
        db.execSQL("DROP TABLE IF EXISTS "+NOMBRE_TABLA);
        db.execSQL(creacion);
    }

    //Añadir nuevo evento
    public  void addEvent(long id_serie, long id_event){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_SERIE, id_serie);
        values.put(KEY_ID_CALENDAR, id_event);

        db.insert(NOMBRE_TABLA,null,values);
        db.close();
    }

    //Buscar Eventos relacionados a una serie
    public int findEvent(long id_serie){
        String selectQuery = "SELECT * FROM "+NOMBRE_TABLA+ " WHERE "+KEY_ID_SERIE+" = "+id_serie+"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        db.close();
        return cursor.getInt(2);
    }

    public long deleteEvent(long id_serie){

        String where = KEY_ID_SERIE+"= '"+id_serie+"'";
        long eventID = findEvent(id_serie);

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NOMBRE_TABLA,where,null);
        db.close();
        return eventID;
    }
}
