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
import android.ucam.edu.seiries.beans.SerieBean;
import android.ucam.edu.seiries.db.EventosDB;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;


public class ActivitiAddSerie extends AppCompatActivity {

    private static final int NOTIF_ID = 1234;
    private static final int REQUEST_IMAGE_OPEN = 1;
    private static final String SERIES_ID = "series";
    private static final String CONTADOR_ID = "count";
    private static final String TAG = "AÑADIR SERIE";
    private Uri fullPhotoUri;
    private ImageButton imageButton;
    private Spinner spinner_dia;
    private Spinner spinner_estado;
    private Switch option_calendar;
    private LottieAnimationView animacion;
    private int dia_seleccionado;
    private int estado_seleccionado;
    private SerieBean nueva_serie;
    private DatabaseReference ref;
    private DatabaseReference seriesRef;
    private DatabaseReference counterRef;
    private StorageReference storageReference;
    private EventosDB dbeventos;
    private int num_caps = -1;
    private int num_picker;
    private int hay_evento;
    private long contador;
    private static int MY_PERMISSIONS_REQUEST_READ_CONTACTS=1;
    private static String[] PERMISSIONS = {Manifest.permission.READ_CALENDAR,Manifest.permission.WRITE_CALENDAR};


    ///// COMIENZO ON CREATE /////
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_serie);

        dbeventos = new EventosDB(this);

        final EditText name = (EditText) findViewById(R.id.editText2);
        final EditText description = (EditText) findViewById(R.id.editText3);
        spinner_dia = (Spinner) findViewById(R.id.spinner_dia);
        spinner_estado = (Spinner) findViewById(R.id.spinner_estado);
        option_calendar = (Switch) findViewById(R.id.switch_calendar);
        ref = FirebaseDatabase.getInstance().getReference();
        seriesRef = ref.child(SERIES_ID);
        counterRef = ref.child(CONTADOR_ID);
        storageReference = FirebaseStorage.getInstance().getReference();
        animacion = (LottieAnimationView) findViewById(R.id.lottieAnimation);
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        spinner_dia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dia_seleccionado = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                dia_seleccionado = -1;
            }
        });

        spinner_estado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                estado_seleccionado = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                estado_seleccionado = -1;
            }
        });

        option_calendar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    calendarPermissions();
                    hay_evento=1;
                }
                else {
                    hay_evento=0;
                }
            }
        });

        final Button btn_add = (Button) findViewById(R.id.button19);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().equals("") || description.getText().toString().equals("") || num_caps == -1 || fullPhotoUri == null) {
                    Intent intencion = new Intent(ActivitiAddSerie.this, FragmentSeriesMain.class);
                    intencion.putExtra("RESULT", "NOPE");
                    Toast.makeText(ActivitiAddSerie.this, "Debes rellenar todos los campos, no olvides seleccionar duración", Toast.LENGTH_SHORT).show();
                    startActivity(intencion);
                } else {
                    nueva_serie = new SerieBean(contador,name.getText().toString(), description.getText().toString(), "", dia_seleccionado, estado_seleccionado, num_caps,hay_evento);

                    activarAnimation();

                    name.setText("");
                    description.setText("");
                    imageButton.setImageResource(R.drawable.imgdefault);

                    StorageReference filePath = storageReference.child("images").child(fullPhotoUri.getLastPathSegment());

                    filePath.putFile(fullPhotoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            nueva_serie.setImageUriString(taskSnapshot.getDownloadUrl().toString());
                            seriesRef.push().setValue(nueva_serie);

                            Uri uri = taskSnapshot.getDownloadUrl();
                            Log.d(TAG, uri.toString());
                            desactivarAnimation();
                            Toast.makeText(ActivitiAddSerie.this,"Gracias por tu aporte!",Toast.LENGTH_SHORT).show();
                        }
                    });

                    ///Añadir el evento al calendario en caso de haber seleccionado la opción
                    if(option_calendar.isChecked()){
                        nuevoEvento(view,nueva_serie.getName(),dia_seleccionado,num_caps,contador);
                        //Lanzamos la notificacion de que se ha creado el evento
                        lanzarNotificacion(nueva_serie.getName());
                    }

                    //Incrementamos en 1 el contador de la ID
                    counterRef.setValue(contador+1);

                    Intent intencion = new Intent(ActivitiAddSerie.this, FragmentSeriesMain.class);
                    intencion.putExtra("RESULT", "OK");
                    startActivity(intencion);
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


        dbeventos.close();
    }

    ///// FIN ON CREATE /////


    @Override
    protected void onStart() {
        super.onStart();

        counterRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contador = dataSnapshot.getValue(Long.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG,"Error al conseguir el ID");
            }
        });
    }

    //Función para abrir el explorador y seleccionar la imagen
    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Selecciona Imagen"), REQUEST_IMAGE_OPEN);
    }

    //Evaluamos lo que nos devuelve la activity de selección de imagen
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_OPEN && resultCode == RESULT_OK) {
            fullPhotoUri = data.getData();

            Log.wtf("INFO", "Resultado OK");

            Glide.with(this)
                    .load(fullPhotoUri)
                    .fitCenter()
                    .centerCrop()
                    .into(imageButton);
        } else {
            Log.wtf("INFO", "Resultado NOPE");
        }
    }

    //Dialog para seleccionar el número de capítulos
    private void numberPickerDialog() {
        NumberPicker numberPicker = new NumberPicker(this);
        numberPicker.setMaxValue(999);
        numberPicker.setMinValue(1);
        NumberPicker.OnValueChangeListener numListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker piker, int oldValue, int newValue) {
                num_picker = newValue;
            }
        };
        numberPicker.setOnValueChangedListener(numListener);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(numberPicker);
        builder.setTitle("Capítulos");
        builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                num_caps = num_picker;
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

    //Agregar un nuevo evento a Calendar mediante Content Provider
    public void nuevoEvento(View v,String name_serie, @Nullable int day, @Nullable int num_caps, long id_serie){

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

            dbeventos.addEvent(id_serie,eventID);

            Log.wtf("ID del Evento", "id  "+eventID);
        }
        catch (SecurityException e){
            Log.wtf("Creando Nuevo Evento",e.toString());
        }

    }




    //Solicitar permisos de lectura y escrituda en calendario
    //en tiempo de ejecución
    private void calendarPermissions() {

        if (ContextCompat.checkSelfPermission(ActivitiAddSerie.this,
                Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(ActivitiAddSerie.this,
                    Manifest.permission.WRITE_CALENDAR) || ActivityCompat.shouldShowRequestPermissionRationale(ActivitiAddSerie.this,
                    Manifest.permission.READ_CALENDAR)) {


            } else {

                ActivityCompat.requestPermissions(ActivitiAddSerie.this, PERMISSIONS, MY_PERMISSIONS_REQUEST_READ_CONTACTS);

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

    public void activarAnimation(){
        animacion.setVisibility(View.VISIBLE);
        animacion.playAnimation();
    }

    public void desactivarAnimation(){
        animacion.pauseAnimation();
        animacion.setVisibility(View.GONE);
    }

}
