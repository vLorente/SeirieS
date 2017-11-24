package android.ucam.edu.seiries;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.ucam.edu.seiries.beans.SerieBean;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FireBaseTestActivity extends AppCompatActivity {

    private static final String MSG_ID = "series";
    private static final int REQUEST_IMAGE_OPEN = 1;
    private static final String TAG = "TEST FIREBASE";

    private EditText name;
    private EditText description;
    private ImageButton imageButton;
    private Button btn_modificar;
    private Button btn_mostrar;
    private Uri fullPhotoUri;
    private DatabaseReference ref;
    private DatabaseReference seriesRef;
    private StorageReference storageReference;
    private ProgressBar progressBar;
    private SerieBean nueva_serie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_base_test);

        btn_modificar = findViewById(R.id.btn_firebaseTest);
        name = findViewById(R.id.name_firebase);
        description = findViewById(R.id.description_firebase);
        imageButton = findViewById(R.id.img_firebase);
        ref = FirebaseDatabase.getInstance().getReference();
        seriesRef = ref.child(MSG_ID);
        storageReference = FirebaseStorage.getInstance().getReference();
        progressBar = findViewById(R.id.progressBarFirebase);
        btn_mostrar = findViewById(R.id.btn_mostrar_firabase);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        btn_modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            String txt_name = name.getText().toString();
            String txt_descrip = description.getText().toString();

            if(fullPhotoUri!=null){
                nueva_serie = new SerieBean(txt_name,txt_descrip,fullPhotoUri.getLastPathSegment(),1,2,12,0);
            } else {
                Toast.makeText(FireBaseTestActivity.this,"No se ha encontrado foto",Toast.LENGTH_SHORT).show();
            }
            activarProgressBar();



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
                    Log.e(TAG,taskSnapshot.getDownloadUrl().toString());
                    desactivarProgressBar();
                    Toast.makeText(FireBaseTestActivity.this,"Serie subida a Firebase",Toast.LENGTH_SHORT).show();
                }
            });

            }
        });

        btn_mostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FireBaseTestActivity.this, FirebaseTestMostrar.class));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        seriesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               long num_series = dataSnapshot.getChildrenCount();
               Log.d(TAG, "El número de series actual es: "+num_series);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Función para abrir el explorador y seleccionar la imagen
    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        //intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Selecciona Imagen"), REQUEST_IMAGE_OPEN);
    }

    //Evaluamos lo que nos devuelve la activity de selección de imagen
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_OPEN && resultCode == RESULT_OK) {
            fullPhotoUri = data.getData();

            Log.wtf("INFO", "Resultado OK");
            imageButton.setImageURI(fullPhotoUri);
        } else {
            Log.wtf("INFO", "Resultado NOPE");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    public void activarProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
        imageButton.setVisibility(View.GONE);
        name.setVisibility(View.GONE);
        description.setVisibility(View.GONE);
        btn_modificar.setVisibility(View.GONE);
    }

    public void desactivarProgressBar(){
        progressBar.setVisibility(View.GONE);
        imageButton.setVisibility(View.VISIBLE);
        name.setVisibility(View.VISIBLE);
        description.setVisibility(View.VISIBLE);
        btn_modificar.setVisibility(View.VISIBLE);
    }

}
