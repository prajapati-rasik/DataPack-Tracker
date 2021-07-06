package bedTimeMode;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.annotation.TransitionRes;
import androidx.core.app.NotificationCompat;

import com.example.datapacktracker.R;

public class NotificationHelper extends ContextWrapper {

    public static final String channel1ID = "channel1ID";
    public static final String getChannel1name = "Channel 1";

    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannels();
        }

    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannels() {
        NotificationChannel channel1 = new NotificationChannel(channel1ID, getChannel1name, NotificationManager.IMPORTANCE_DEFAULT);
        channel1.enableLights(true);
        channel1.enableVibration(true);
        channel1.setLightColor(R.color.design_default_color_primary);
        channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(channel1);
    }

    public  NotificationManager getManager(){
        if(mManager == null){
            mManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return  mManager;
    }

    public NotificationCompat.Builder getChannel1Notification(String title, String message)//
    {
        //updated code
        /*Intent activityIntent = new Intent(this,NotificationHelper.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,0, activityIntent, 0);

        Intent broadcastIntent = new Intent(this, AlertReceiver.class);
        broadcastIntent.putExtra("Bedtime", message);
        PendingIntent actionIntent = PendingIntent.getBroadcast(this,0,broadcastIntent,PendingIntent.FLAG_UPDATE_CURRENT);*/

        //Intent for yes button
        Intent intent1 = new Intent(NotificationHelper.this,bedTimeMode.class);
        intent1.putExtra("Yes",true);
        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent1 = PendingIntent.getActivity(NotificationHelper.this,0,intent1,PendingIntent.FLAG_ONE_SHOT);
        //intent for no button

        Intent intent2 = new Intent(NotificationHelper.this,bedTimeMode.class);
        intent2.putExtra("No",true);
        intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent2 = PendingIntent.getActivity(NotificationHelper.this,1,intent2,PendingIntent.FLAG_ONE_SHOT);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        return new NotificationCompat.Builder(getApplicationContext(), channel1ID)
                .setContentTitle(title)// title, String message
                .setContentText(message)//message
                .setSmallIcon(R.drawable.ic_baseline_bedtime_24)
                .setSound(uri)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.GREEN)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_launcher_foreground,"Yes",pendingIntent1)
                .addAction(R.drawable.ic_launcher_foreground,"No",pendingIntent2)
                //.setContentIntent(contentIntent)
                //.addAction(R.mipmap.ic_launcher, "Accept", actionIntent)
                ;
    }
}