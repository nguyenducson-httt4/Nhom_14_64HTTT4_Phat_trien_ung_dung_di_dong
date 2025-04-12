package tlu.cse.ht64.apphoctienganh.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import tlu.cse.ht64.apphoctienganh.R;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "EnglishLearningChannel";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "English Learning", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Time to Learn English!")
                .setContentText("Open the app and practice some vocabulary.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(android.provider.Settings.System.DEFAULT_NOTIFICATION_URI)
                .setAutoCancel(true);

        notificationManager.notify(1, builder.build());
    }
}