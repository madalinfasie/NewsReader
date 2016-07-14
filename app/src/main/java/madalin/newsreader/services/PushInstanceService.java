package madalin.newsreader.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by madalin2 on 09.07.2016.
 */
public class PushInstanceService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        Log.i("PUSH", "Tocken: " + FirebaseInstanceId.getInstance().getToken());

    }
}
