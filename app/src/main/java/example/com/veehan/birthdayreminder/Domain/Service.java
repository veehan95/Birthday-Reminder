package example.com.veehan.birthdayreminder.Domain;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import example.com.veehan.birthdayreminder.R;
import example.com.veehan.birthdayreminder.UserInterface.DisplayBirthdayStar;
import example.com.veehan.birthdayreminder.UserInterface.MainActivity;

/**
 * Created by Tan Vee Han 1304713 on 27/8/2017.
 * This is the service class to generate a notification
 */

public class Service extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int num = intent.getIntExtra(MainActivity.EXTRA_BIRTHDAYSTAR, 0);

        // Generate message base on number of birthday star
        String message = "";
        if(num > 1) {
            message = "There are a total of "+Integer.toString(num)+" birthday stars today";
        }
        else if(num == 1){
            message = "There are only 1 birthday star today";
        }
        else{
            message = "There are no birthday star today";
        }

        Intent notificationIntent = new Intent(context, DisplayBirthdayStar.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT
                );

        NotificationManager notificationManager = (NotificationManager)context.getSystemService
                (Context.NOTIFICATION_SERVICE);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(resultPendingIntent)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Birthday Reminder")
                .setContentText(message)
                .setAutoCancel(true);

        // Only show notification only if at least 1 birthday
        if(num > 0) {
            notificationManager.notify(100, builder.build());
        }
    }
}
