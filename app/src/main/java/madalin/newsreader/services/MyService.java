package madalin.newsreader.services;

import android.app.Service;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by madalin2 on 09.07.2016.
 */
public class MyService extends Service {
    private static final String TAG = MyService.class.getSimpleName();
    private final OkHttpClient okHttpClient = new OkHttpClient();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

       // startForeground(); //putem sa tinem procesul mai mult timp, ne trebuie si o notificare
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: "+intent);
        sendRequest();

        return  START_NOT_STICKY;
        //START_NON_STICKY //L-A INCHIS, PA PA
        //START_STICKY     //PORNESTE DACA NE OMOARA SERVICE-UL
        // START_REDELIVER_INTENT; //PORNESTE DIN NOU SERVICE-UL SI NE TRIMITE UN INTENT CU CARE A FOST DESCHIS ULTIMA OARA
    }

    private void sendRequest(){
        //http://github.com/square/okhttp/wiki/recipes -> wiki -> recipes

        String data = "this is a test";
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), data);
        Request request  = new Request.Builder().url("http://httpbin.org/post").post(body).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, e.getMessage(),e);
                stopSelf();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "Response: " + response.body().string());
                stopSelf();
            }
        });

    }


}
