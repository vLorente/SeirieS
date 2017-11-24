package android.ucam.edu.seiries;

import android.app.Application;

import com.facebook.appevents.AppEventsLogger;


public class SeirieSApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }
}
