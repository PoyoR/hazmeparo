package Clases;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hazmeparo.R;

import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.i("NUEVO TOKEN", s);

        //Mandar a servidor
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> data = remoteMessage.getData();
        String titulo = data.get("titulo");
        String body = data.get("body");

        Log.i("NUEVA NOTIFICACION", "titulo: "+titulo);
        Log.i("NUEVA NOTIFICACION", "body: "+body);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Hazme Paro";
            String description = "Notificaciones Hazme Paro";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1907", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1907")
                .setSmallIcon(R.drawable.hazmelogo)
                .setContentTitle(titulo)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManager.notify(1907, builder.build());
    }
}
