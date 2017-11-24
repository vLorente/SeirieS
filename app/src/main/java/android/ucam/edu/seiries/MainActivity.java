package android.ucam.edu.seiries;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.ucam.edu.seiries.beans.Serie;
import android.ucam.edu.seiries.db.SeriesDB;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private TextView labelUserName;
    private TextView labelUserID;
    private TextView labelEmail;
    private ImageView imgProfile;
    private GoogleApiClient googleApiClient;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        labelUserID = findViewById(R.id.label_id);
        labelUserName = findViewById(R.id.label_user);
        labelEmail = findViewById(R.id.label_email);
        imgProfile = findViewById(R.id.img_profile);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //LOGIN SILENCIOSO GOOGLE
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        //FIN LOGIN SILENCIOSO GOOGLE

        //Mostar los datos de usuario desde Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        //Listener que escucha los cambios en la atutenticación de firebase
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //Comprobamos que tenemos un usuario autenticado
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    //En caso de tenerlo pintamos sus datos
                    displayProfileInfo(user);
                } else {
                    //En caso de que no tengamos vamos al login
                    goLoginScreen();
                }
            }
        };



        //CERRAR SESION
        final Button btn_close_session = findViewById(R.id.btn_close_session);
        btn_close_session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                logoutFacebook();
                logoutGoogle(view);
            }
        });

        //FIN CERRAR SESION

        //CREAMOS BASE DE DATOS Y AÑADIMOS 3 SERIES
        SeriesDB db = new SeriesDB(this);
        Serie serie = new Serie("Juego de Tronos","Juego de tronos sigue las múltiples líneas argumentales de Canción de hielo y fuego. La mayor parte de la historia tiene lugar en Poniente, un continente ficticio donde las estaciones pueden durar años, y se centra en las violentas luchas dinásticas que surgen entre varias familias nobiliarias por el control del Trono de Hierro.",R.drawable.got,1, 2,12,0);
        Serie serie2 = new Serie("Rick y Morty","Rick Sánchez es la definición exacta de \"científico loco\". Es alcohólico, un genio, es irresponsable, un poco depresivo, egoista y está loco. Rick acaba de mudarse a casa de su hija Beth y allí recuerda que tiene un nieto llamado Morty. Sin preguntar a nadie, decide que le va a obligar a acompañarle a todo tipo de aventuras para que el chico se vuelva inteligente como él y no se convierta en un idiota como Jerry, padre de Morty y yerno de Rick. Así, Rick y Morty comienzan a vivir aventuras intergalácticas a pesar de que la familia no quiere que lo sigan haciendo. Poco a poco tienen que intentar encontrar un equilibrio entre su vida familiar y sus viajes a través del espacio y por distintas realidades paralelas, algo que no es fácil para el pequeño Morty que es incapaz de tener una vida normal al margen de su abuelo.",R.drawable.rm,2,2,13,0);
        Serie serie3 = new Serie ("Halt and Catch Fire","Situado en la década de los 80, la serie dramática trata del boom de la informática a través de los ojos de un visionario empresarial,Gordon, un ingeniero informático; y Cameron Howe, una joven prodigio de la programación. Las innovaciones de este trío se enfrentarán directamente con los gigantes corporativos de la época. Su asociación profesional y personal serán desafiados por la avaricia y el ego mientras cambien la cultura de Silicon Prairie, Texas.",R.drawable.hacf,4,2,12,0);

        if(db.isEmpty()){
            db.addSerie(serie);
            db.addSerie(serie2);
            db.addSerie(serie3);
        }
        db.close();
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }



    private void goLoginScreen() {

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void logoutFacebook(){
        LoginManager.getInstance().logOut();
        goLoginScreen();
    }

    private void logoutGoogle(View view){
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if(status.isSuccess()){
                    goLoginScreen();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.not_logout_google,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, Preferencias.class));
                return true;
            case R.id.menu_exit:
                finishAffinity();
                return true;
            case R.id.menu_about:
                startActivity(new Intent(this,AcercaDe.class));
                return true;
            case R.id.menu_firebase:
                startActivity(new Intent(this, FireBaseTestActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_diary) {

        } else if (id == R.id.nav_series) {
            startActivity(new Intent(this,FragmentSeriesMain.class));
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this,AcercaDe.class));
        } else if (id == R.id.nav_Pantalla) {
            startActivity(new Intent(this,MultiTouchActivity.class));
        } else if (id == R.id.nav_sensors) {
            startActivity(new Intent(this,SensorActivity.class));
        } else if (id == R.id.nav_multimedia){
            startActivity(new Intent(this, MultimediaActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Método para pintar en pantalla los datos del usuario
    private void displayProfileInfo(FirebaseUser user) {
        if(user != null){
            String id = user.getUid();
            String name = user.getDisplayName();
            String email = user.getEmail();
            labelUserName.setText(name);
            labelUserID.setText(id);
            labelEmail.setText(email);
            Glide.with(getApplicationContext())
                    .load(user.getPhotoUrl())
                    .crossFade()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.5f)
                    .into(imgProfile);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,R.string.login_error,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(firebaseAuthListener);
    }
}
