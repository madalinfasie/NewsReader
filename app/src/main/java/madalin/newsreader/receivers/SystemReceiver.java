package madalin.newsreader.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

import madalin.newsreader.services.MyService;

/**
 * Created by madalin2 on 09.07.2016.
 */
public class SystemReceiver extends BroadcastReceiver {

    private static final String TAG = SystemReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if(context == null || intent == null) {
            return;
        }
            String action = intent.getAction();
            Log.e(TAG, "Am primit in Receiver actiunea: "+action);
            switch (action){
                case Intent.ACTION_USER_PRESENT:     //daca deblochez ecranul             //diferite actiuni de la actiuni de boot, la actiuni de alarma
                    context.startService(new Intent(context, MyService.class));
                    Log.e(TAG, "User present! ");
                    break;
                case WifiManager.WIFI_STATE_CHANGED_ACTION:
                    Log.e(TAG, "Wifi state changes! ");
                    break;
                }
    }
}
