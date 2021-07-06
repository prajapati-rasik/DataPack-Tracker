package Emergency_DataBank;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.CalendarContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.datapacktracker.R;

import java.util.Calendar;

import Usage_calculation.makeCalenderInstances;
import Usage_calculation.models.dailyUsageModel;

public class  computeDataThread extends Thread {

    public static final String channel2ID = "channel2ID";
    public static final String getChannel2name = "Channel 2";
    public static final int notificationId = 2;
    static Context context;
    long timeLimit;
    long dataLimit;
    SharedPreferences.Editor editor;

    public computeDataThread(long datavalue, long timevalue){
        timeLimit = timevalue;
        dataLimit = datavalue;
    }

    static boolean cancelled = false;

    private NotificationManager notificationManager;

    public static void setCancelled(){
        cancelled = true;
    }

    public static void setContext(Context context1){ context = context1;}

    @Override
    public void run() {
        editor = context.getSharedPreferences("Emergency_Databank", Context.MODE_PRIVATE).edit();
        Log.e("nfsjdls","data_limit"+dataLimit);
        Log.e("fsfd","time_limit"+timeLimit);
        SharedPreferences sharedPreferences = context.getSharedPreferences("MultipleSim", Context.MODE_PRIVATE);
        String id1 = sharedPreferences.getString("subscriberId1", null);
        String id2 = sharedPreferences.getString("subscriberId2", null);
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        NetworkStats.Bucket bucket = null, bucket1 = null;
        boolean isCompleted = false;
        while(!isCompleted && !cancelled){
            try {
                dailyUsageModel model = makeCalenderInstances.today(context);
                try {
                    if(id1 != null)
                        bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE, id1, model.getStart().getTimeInMillis(), model.getEnd().getTimeInMillis());
                    if(id2 != null)
                        bucket1 = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE, id2, model.getStart().getTimeInMillis(), model.getEnd().getTimeInMillis());
                    long total = 0;
                    if(bucket != null)  total += bucket.getTxBytes() + bucket.getTxBytes();
                    if(bucket1 != null) total += bucket1.getTxBytes() + bucket1.getRxBytes();
                    Log.e("lfjjds","total"+total);
                    if(total >= dataLimit) isCompleted = true;
                    Log.e("dfjdl","completed"+isCompleted);
                    if(Calendar.getInstance().getTimeInMillis() >= timeLimit) isCompleted = true;
                    Log.e("fdfdf","timeLimit : "+timeLimit+": currenttime : "+Calendar.getInstance().getTimeInMillis());
                    Log.e("dfjdl","completed"+isCompleted);
                } catch (RemoteException | SecurityException ignored) {
                }
                Log.e("hii", "slept.....");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                isCompleted = true;
            }
        }
        if(isCompleted || cancelled){
            editor.putBoolean("isrunning", false);
            editor.putString("data_value", null);
            editor.putString("time_value", null);
            editor.apply();
            cancelled = false;
            Log.e("fjsfi","completed");
        }
        if(isCompleted){
            createNotificationChannel();
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channel2ID)
                    .setSmallIcon(R.drawable.ic_datapack_tracker_logo)
                    .setContentTitle("Emergency Databank")
                    .setContentText("Limits reached in Databank. Databank is turned off.")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(context.getColor(R.color.colorPrimary))
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setAutoCancel(true);
            notificationManager.notify(notificationId, builder.build());
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(channel2ID, getChannel2name, NotificationManager.IMPORTANCE_DEFAULT);
            channel1.enableLights(true);
            channel1.enableVibration(true);
            channel1.setLightColor(R.color.design_default_color_primary);
            channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            getManager().createNotificationChannel(channel1);
        }
    }

    public  NotificationManager getManager(){
        notificationManager =  (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        return notificationManager;
    }
}