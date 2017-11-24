package android.ucam.edu.seiries.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Valentín Lorente Jiménez on 23/11/2017.
 * Copyright © 2017 vLorente. All rights reserved.
 */

public class MiFirebaseInstanceIdService extends FirebaseInstanceIdService{

    private static final String TAG = "NOTIFICACIÓN_REMOTA";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String token = FirebaseInstanceId.getInstance().getToken();

        Log.d(TAG, "Llega noficicación con token: "+token);
    }
}
