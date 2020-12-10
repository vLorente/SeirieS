package android.ucam.edu.seiries;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private LoginButton loginButton;
    private CallbackManager callBackManager;
    private GoogleApiClient googleApiClient;
    private SignInButton signInButton;
    private static final int SIGN_IN_CODE=777;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private ImageView imageView;
    private TextView textView;
    private LottieAnimationView animation1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callBackManager = CallbackManager.Factory.create();
        imageView = findViewById(R.id.imageView2);
        textView = findViewById(R.id.textView3);
        animation1 = findViewById(R.id.animationLogin1);

        //GOOGLE
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButton = findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, SIGN_IN_CODE);
            }
        });
        //FIN GOOGLE

        //FACEBOOK
        loginButton = findViewById(R.id.loginButton);
        loginButton.setReadPermissions(Arrays.asList("email"));
        loginButton.registerCallback(callBackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //Pasamos a la autenticacion den Firebase
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),R.string.login_cancel,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),R.string.login_error,Toast.LENGTH_SHORT).show();
            }
        });
        //FIN FACEBOOK

        //FIREBASE
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //Detectamos si ya tenemos un usuario logueado
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    goMainScreen();
                }
            }
        };
        //FIN FIREBASE
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SIGN_IN_CODE){
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(googleSignInResult);
        }else {
            callBackManager.onActivityResult(requestCode,resultCode,data);
        }
    }

    //Manejamos el resultado del login con GOOGLE
    private void handleSignInResult(GoogleSignInResult googleSignInResult) {
        if(googleSignInResult.isSuccess()){
            firebaseAuthWhitGoogle(googleSignInResult.getSignInAccount());
        } else {
            Toast.makeText(this,R.string.login_not_success,Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWhitGoogle(GoogleSignInAccount signInAccount) {
        //Lanzamos el Lottie
        activarAnimacion();

        AuthCredential credential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //Paramos el Lottie
                desactivarAnimacion();

                //Si ocurre algún problema durante el login en firebase
                if(!task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),R.string.login_error,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        //Lanzamos el Lottie
        activarAnimacion();

        //Creamos la credencial
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        //Iniciamos sesion en Firebase con la credencial que nos llega
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //Si ocurre algún problema durante el login en firebase
                if(!task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),R.string.login_error,Toast.LENGTH_SHORT).show();
                }
                //Paramos el Lottie
                desactivarAnimacion();
            }
        });


    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,R.string.login_error,Toast.LENGTH_SHORT).show();
    }

    private void goMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(firebaseAuthListener);
    }

    public void activarAnimacion(){
        animation1.playAnimation();
        animation1.setVisibility(View.VISIBLE);
        signInButton.setVisibility(View.GONE);
        loginButton.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
    }


    public void desactivarAnimacion(){
        animation1.setVisibility(View.GONE);
        signInButton.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
        animation1.pauseAnimation();
    }
}
