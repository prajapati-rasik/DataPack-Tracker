package bedTimeMode;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.datapacktracker.R;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class foregroundService extends Service {

    public static final String channel1ID = "channel1ID";
    public static final String getChannel1name = "Channel 1";
    String title = "BedTime";
    String message = "Please Turn Off your Data";
    private NotificationManager mManager;
    Thread thread;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        long cal = intent.getLongExtra("time", Calendar.getInstance().getTimeInMillis());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels();
        }
        Intent notificationIntent = new Intent(this, bedTimeMode.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        @SuppressLint("DefaultLocale")
        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(cal),
                TimeUnit.MILLISECONDS.toMinutes(cal) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(cal) % TimeUnit.MINUTES.toSeconds(1));
        Notification notification = new NotificationCompat.Builder(this, channel1ID)
                .setContentTitle("BedTimeMode")
                .setContentText("alarm set for " + hms)
                .setSmallIcon(R.drawable.ic_baseline_bedtime_24)
                .setContentIntent(pendingIntent)
                .build();
        thread = new Thread(() -> {
            while(true){
                Calendar c = Calendar.getInstance();
                if(c.getTimeInMillis() >= cal){
                    Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Notification notification1 = new NotificationCompat.Builder(foregroundService.this, channel1ID)
                            .setContentTitle(title)
                            .setContentText(message)
                            .setSmallIcon(R.drawable.ic_baseline_bedtime_24)
                            .setSound(uri)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                            .setColor(Color.GREEN)
                            .setAutoCancel(true)
                            .build();
                    NotificationManagerCompat manager = NotificationManagerCompat.from(foregroundService.this);
                    manager.notify(1, notification1);
                    break;
                }else{
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        startForeground(1, notification);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        thread.stop();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannels() {
        NotificationChannel channel1 = new NotificationChannel(channel1ID, getChannel1name, NotificationManager.IMPORTANCE_DEFAULT);
        channel1.enableLights(true);
        channel1.enableVibration(true);
        channel1.setLightColor(R.color.design_default_color_primary);
        channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        mManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        mManager.createNotificationChannel(channel1);
    }
}
