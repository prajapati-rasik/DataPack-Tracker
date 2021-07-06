//package bedTimeMode;
//
//import android.annotation.TargetApi;
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.media.RingtoneManager;
//import android.net.Uri;
//import android.os.Build;
//
//import androidx.core.app.NotificationCompat;
//import androidx.core.app.NotificationManagerCompat;
//
//import com.example.datapacktracker.R;
//
//public class AlertReceiver extends BroadcastReceiver {
//
//    public static final String channel1ID = "channel1ID";
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        String title = "BedTime";
//        String message = "Please Turn Off your Data";
//
//        Intent intent1 = new Intent(context,bedTimeMode.class);
//        intent1.putExtra("Yes",true);
//        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
//        PendingIntent pendingIntent1 = PendingIntent.getActivity(context,0,intent1,PendingIntent.FLAG_ONE_SHOT);
//
//        Intent intent2 = new Intent(context, bedTimeMode.class);
//        intent2.putExtra("No",true);
//        intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
//        PendingIntent pendingIntent2 = PendingIntent.getActivity(context,1,intent2,PendingIntent.FLAG_ONE_SHOT);
//
//        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channel1ID)
//                .setContentTitle(title)
//                .setContentText(message)
//                .setSmallIcon(R.drawable.ic_baseline_bedtime_24)
//                .setSound(uri)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                .setColor(Color.GREEN)
//                .setAutoCancel(true)
//                .addAction(R.drawable.ic_launcher_foreground,"Yes",pendingIntent1)
//                .addAction(R.drawable.ic_launcher_foreground,"No",pendingIntent2)
//                //.setContentIntent(contentIntent)
//                //.addAction(R.mipmap.ic_launcher, "Accept", actionIntent)
//                ;
//
//        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
//        manager.notify(1, builder.build());
//    }
//}