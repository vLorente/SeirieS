package android.ucam.edu.seiries;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.ucam.edu.seiries.beans.Serie;
import android.ucam.edu.seiries.db.EventosDB;
import android.ucam.edu.seiries.db.SeriesDB;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


public class ActivityUpdateSerie extends AppCompatActivity {

    static final int REQUEST_IMAGE_OPEN = 1;
    private static final String DEBUG_TAG = "ActivityUpdateSerie";
    private static final int NOTIF_ID = 1234;
    private Uri fullPhotoUri;
    private ImageButton imageButton;
    private Spinner spinner_dia;
    private Spinner spinner_estado;
    private int dia_seleccionado;
    private int estado_seleccionado;
    private SeriesDB db;
    private int id_serie;
    private Serie last;
    private int num_caps=-1;
    private int num_picker;
    private TextView txt_caps;
    private Switch switch_evento;
    private int hay_evento;
    private static int MY_PERMISSIONS_REQUEST_READ_CONTACTS=1;
    private static String[] PERMISSIONS = {Manifest.permission.READ_CALENDAR,Manifest.permission.WRITE_CALENDAR};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_serie);

        db = new SeriesDB(this);

        //Recuperamos el ID de la intencion
        id_serie=getIntent().getExtras().getInt("ID");
        //Buscamos la serie a editar por su ID
        last=db.getSerieById(id_serie);
        num_caps=last.getNum_capitulos();

        final EditText name = (EditText) findViewById(R.id.editText2);
        final EditText description = (EditText) findViewById(R.id.editText3);
        spinner_dia = (Spinner) findViewById(R.id.spinner_dia);
        spinner_estado= (Spinner) findViewById(R.id.spinner_estado);
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        txt_caps = (TextView) findViewById(R.id.txt_caps);
        switch_evento = (Switch) findViewById(R.id.switch_ac_evento);

        //Cargamos los datos anteriores de la serie en los campos para poder editarlos
        spinner_dia.setSelection(last.getDia_salida());
        spinner_estado.setSelection(last.getEstadoSerie());
        name.setText(last.getName());
        description.setText(last.getDescription());
        if(last.getImageId()==-1){
            imageButton.setImageURI(last.getImageUri());
        }else {
            imageButton.setImageResource(last.getImageId());
        }
        txt_caps.setText("Número de capítulos actual: "+num_caps);
        if(last.getEvento()==1){
            switch_evento.setChecked(true);
        }else{
            switch_evento.setChecked(false);
        }



        //Llamada a la funcion para seleccionar imagen
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        spinner_dia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dia_seleccionado=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                dia_seleccionado=-1;
            }
        });

        spinner_estado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                estado_seleccionado=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                estado_seleccionado=-1;
            }
        });

        switch_evento.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    calendarPermissions();
                    hay_evento=1;
                }else {
                    android.app.AlertDialog.Builder altdial = new android.app.AlertDialog.Builder(ActivityUpdateSerie.this);
                    altdial.setMessage("¿Seguro qué quieres eliminar el evento?").setCancelable(false)
                            .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                   hay_evento=0;
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch_evento.setChecked(true);
                                    dialog.cancel();
                                }
                            });

                    android.app.AlertDialog alert = altdial.create();
                    alert.setTitle("Cancelar Recordatorio");
                    alert.show();
                }
            }
        });

        final Button btn_add = (Button) findViewById(R.id.button19);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().equals("") || description.getText().toString().equals("") || num_caps==-1){

                    Intent intencion=new Intent(ActivityUpdateSerie.this,FragmentSeriesMain.class);
                    intencion.putExtra("RESULT","NOPE");
                    Toast.makeText(ActivityUpdateSerie.this,"Debes rellenar todos los campos, no olvides seleccionar duración",Toast.LENGTH_SHORT).show();
                    startActivity(intencion);

                }
                else {
                    if(fullPhotoUri!=null){
                        Serie serie = new Serie(name.getText().toString(),description.getText().toString(),fullPhotoUri,dia_seleccionado,estado_seleccionado,num_caps,hay_evento);
                        db.updateSerie(id_serie,serie);
                        Intent intencion=new Intent(ActivityUpdateSerie.this,FragmentSeriesMain.class);
                        intencion.putExtra("RESULT","OK");
                        startActivity(intencion);
                    }
                    else{
                        Serie serie;
                        if(last.getImageId()==-1){
                            serie = new  Serie(name.getText().toString(),description.getText().toString(),last.getImageUri(),dia_seleccionado,estado_seleccionado,num_caps,hay_evento);
                        }else {
                            serie = new  Serie(name.getText().toString(),description.getText().toString(),last.getImageId(),dia_seleccionado,estado_seleccionado,num_caps,hay_evento);
                        }

                        db.updateSerie(id_serie,serie);

                        //Controlamos si cambia hay_evento, es decir, si se decide desactivarlo o activarlo si se
                        // encontraba en el estado contrario
                        if(last.getEvento()==1 && hay_evento==0){
                            EventosDB eventosDB = new EventosDB(ActivityUpdateSerie.this);
                            long eventID=eventosDB.deleteEvent(last.getId());

                            //Mi Toast Personalizado
                            Toast miToast = new Toast(ActivityUpdateSerie.this);
                            LayoutInflater layoutInflater = getLayoutInflater();
                            View layout = layoutInflater.inflate(R.layout.mytoast, (ViewGroup) findViewById(R.id.mytoast));
                            TextView txtMsg = (TextView)layout.findViewById(R.id.txtMensaje);
                            txtMsg.setText(getString(R.string.toastText));
                            miToast.setDuration(Toast.LENGTH_SHORT);
                            miToast.setGravity(Gravity.CENTER|Gravity.CENTER,0,0);
                            miToast.setView(layout);
                            miToast.show();

                            deleteEventById(eventID);
                            eventosDB.close();
                        }
                        else if(last.getEvento()==0 && hay_evento==1){
                            nuevoEvento(view,serie.getName(),dia_seleccionado,num_caps,last.getId());
                            Log.wtf("Actualizando serie","Se ha creado el evento");
                            //Lanzamos la notificacion del nuevo evento
                            lanzarNotificacion(serie.getName());
                        }
                        ///fin contol

                        Intent intencion=new Intent(ActivityUpdateSerie.this,FragmentSeriesMain.class);
                        intencion.putExtra("RESULT","OK");
                        startActivity(intencion);
                    }

                }

            }
        });

        final Button btn_caps = (Button) findViewById(R.id.btn_capitulos);
        btn_caps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberPickerDialog();
            }
        });

        db.close();
    }

    //Función para abrir el explorador y seleccionar la imagen
    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent,"Selecciona Imagen"), REQUEST_IMAGE_OPEN);
    }
    //Evaluamos lo que nos devuelve la activity de selección de imagen
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_OPEN && resultCode == RESULT_OK){
            fullPhotoUri = data.getData();

            Log.wtf("INFO","Resultado OK");
            imageButton.setImageURI(fullPhotoUri);
        }
        else {
            Log.wtf("INFO","Resultado NOPE");
        }
    }

    private void numberPickerDialog(){
        NumberPicker numberPicker = new NumberPicker(this);
        numberPicker.setMaxValue(999);
        numberPicker.setMinValue(1);
        NumberPicker.OnValueChangeListener numListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker piker, int oldValue, int newValue) {
                //Log.wtf("Nuevo numero del numpicker","El numero es "+newValue);
                num_picker=newValue;
            }
        };
        numberPicker.setOnValueChangedListener(numListener);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(numberPicker);
        builder.setTitle("Capítulos");
        builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                num_caps=num_picker;
                //Log.wtf("Numero del numpicker","El numero es "+num_caps);
                txt_caps.setText("Número de capítulos actual: "+num_caps);
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }


    //Eliminar un evento del calendario
    public void deleteEventById (long eventID){
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        Uri deleteUri = null;
        deleteUri = ContentUris.withAppendedId(Events.CONTENT_URI, eventID);
        int rows = getContentResolver().delete(deleteUri, null, null);
        Log.i(DEBUG_TAG, "Rows deleted: " + rows);
    }


    //Agregar un nuevo evento a Calendar mediante Content Provider
    public void nuevoEvento(View v, String name_serie,@Nullable int day, @Nullable int num_caps, int id_serie){

        int permisos_escritura=checkPermission(Manifest.permission.WRITE_CALENDAR, 10515,10157);
        int permisos_lectura=checkPermission(Manifest.permission.READ_CALENDAR, 10515,10157);

        Log.wtf("Permisos de escritura","El resultado del chequeo ha sido: "+permisos_escritura);
        Log.wtf("Permisos de lectura","El resultado del chequeo ha sido: "+permisos_lectura);

        try{
            long calID = 1;
            Calendar mCalendar = Calendar.getInstance();
            int i = mCalendar.get(Calendar.WEEK_OF_MONTH);
            mCalendar.set(Calendar.WEEK_OF_MONTH, ++i);

            int dayMod = (day+2)%7;
            Log.wtf("Resultado", "La suma modulo 7 es: "+dayMod);
            mCalendar.set(Calendar.DAY_OF_WEEK,dayMod);
            mCalendar.set(Calendar.HOUR_OF_DAY,15);


            long my_day_event = mCalendar.getTimeInMillis();
            long end_event = my_day_event + 1000 * 60 * 60; // For next 1hr

            ContentResolver cr = getContentResolver();
            ContentValues values = new ContentValues();
            values.put(Events.DTSTART, my_day_event);
            values.put(Events.DTEND, end_event);
            values.put(Events.RRULE,"FREQ=WEEKLY;COUNT="+num_caps);
            values.put(Events.TITLE, name_serie);
            values.put(Events.DESCRIPTION, "La duración de la serie es de "+num_caps+"capítulos.");
            values.put(Events.CALENDAR_ID, calID);
            values.put(Events.EVENT_TIMEZONE, "Europe/Madrid");
            Uri uri = cr.insert(Events.CONTENT_URI, values);

            // id del evento importante guardar proximament en base de datos vinculado a la serie
            long eventID = Long.parseLong(uri.getLastPathSegment());

            //Guardamos el evento en base de datos
            EventosDB dbeventos = new EventosDB(this);
            dbeventos.addEvent(id_serie,eventID);
            dbeventos.close();

            Log.wtf("ID del Evento", "id  "+eventID);
        }
        catch (SecurityException e){
            Log.wtf("Creando Nuevo Evento",e.toString());
        }

    }

    //Solicitar permisos de lectura y escrituda en calendario
    //en tiempo de ejecución
    private void calendarPermissions() {
        if (ContextCompat.checkSelfPermission(ActivityUpdateSerie.this,
                Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(ActivityUpdateSerie.this,
                    Manifest.permission.WRITE_CALENDAR) || ActivityCompat.shouldShowRequestPermissionRationale(ActivityUpdateSerie.this,
                    Manifest.permission.READ_CALENDAR)) {


            } else {

                ActivityCompat.requestPermissions(ActivityUpdateSerie.this, PERMISSIONS, MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            }
        }
    }

    public void lanzarNotificacion(String name_serie){
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_logo)
                        .setContentTitle(getString(R.string.notification_title))
                        .setContentText(name_serie)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        //Color del led de notificacion
                        .setLights(Color.CYAN, 1, 0)
                        //Sonido de la notificacion
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setAutoCancel(true);
        //Creamos un intent para llamar a la nueva activity

        Uri.Builder uri = CalendarContract.CONTENT_URI.buildUpon();
        uri.appendPath("time");
        ContentUris.appendId(uri, Calendar.getInstance().getTimeInMillis());
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setData(uri.build());

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);


        NotificationManager notificationManager =  (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIF_ID, builder.build());

    }

}
