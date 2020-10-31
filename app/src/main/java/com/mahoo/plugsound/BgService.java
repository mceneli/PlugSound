package com.mahoo.plugsound;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import java.util.Locale;

import static com.mahoo.plugsound.MainActivity.MyPREFERENCES;


public class BgService extends Service {
    private static final int NOTIF_ID = 1;
    private static final String NOTIF_CHANNEL_ID = "AppNameBackgroundService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        //Add broadcast receiver for plugging in and out here
        ChargeDetection chargeDetector = new ChargeDetection(); //This is the name of the class at the bottom of this code.

        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        this.registerReceiver(chargeDetector, filter);

        startForeground();

        return super.onStartCommand(intent, flags, startId);
    }

    private void startForeground() {
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        startForeground(NOTIF_ID, new NotificationCompat.Builder(this,
                NOTIF_CHANNEL_ID) // don't forget create a notification channel first
                .setOngoing(true)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Background service is running")
                .setContentIntent(pendingIntent)
                .build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    NOTIF_CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}

class ChargeDetection extends BroadcastReceiver {
    private String connectSound;
    private String disconnectSound;
    TextToSpeech t1;

    @Override
    public void onReceive(final Context context, Intent intent) {
        //Now check if user is charging here. This will only run when device is either plugged in or unplugged.
        SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String toSpeak = "";
        if(intent.getAction() == intent.ACTION_POWER_CONNECTED){
            toSpeak += prefs.getString("connect","a");
            //Toast.makeText(context,prefs.getString("connect","a"),Toast.LENGTH_SHORT).show();
        }
        else if(intent.getAction() == intent.ACTION_POWER_DISCONNECTED){
            toSpeak += prefs.getString("disconnect","a");
            //Toast.makeText(context,"disconnected",Toast.LENGTH_SHORT).show();
        }

        TtsProviderFactory ttsProviderImpl = TtsProviderFactory.getInstance();
        if (ttsProviderImpl != null) {
            ttsProviderImpl.init(context);
            ttsProviderImpl.say(toSpeak);
        }

    }
}
