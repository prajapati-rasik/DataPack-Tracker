package bedTimeMode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.datapacktracker.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class bedTimeMode extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bedtimemode);
        mTextView = findViewById(R.id.textViewBTM);
        Button buttonTimepicker = findViewById(R.id.button_timepicker);
        Button buttonCancelAlarm = findViewById(R.id.button_cancle);
        if(isServiceRunning()){
            buttonTimepicker.setVisibility(View.GONE);
            buttonCancelAlarm.setVisibility(View.VISIBLE);
        }
        buttonTimepicker.setOnClickListener(v -> {
            TimePickerDialog timePicker;
            Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            timePicker = new TimePickerDialog(this, this,hour, minute, android.text.format.DateFormat.is24HourFormat(this));
            timePicker.show();
            buttonTimepicker.setVisibility(View.GONE);
            buttonCancelAlarm.setVisibility(View.VISIBLE);
        });
        buttonCancelAlarm.setOnClickListener(v -> {
            cancelAlarm();
            buttonCancelAlarm.setVisibility(View.GONE);
            buttonTimepicker.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        updateTimeText(c);
        startAlarm(c);
    }

    private void updateTimeText(Calendar c) {
        String timeText = "Alarm On";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        mTextView.setText(timeText);
    }

    private void startAlarm(Calendar c){
        Intent serviceIntent = new Intent(this, foregroundService.class);
        serviceIntent.putExtra("time", c.getTimeInMillis());
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    private  void cancelAlarm(){
        Intent serviceIntent = new Intent(this, foregroundService.class);
        stopService(serviceIntent);
        mTextView.setText("No Alarm Set");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private boolean isServiceRunning(){
        boolean serviceRunning = false;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> l = am.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo runningServiceInfo : l) {
            if (runningServiceInfo.service.getClassName().equals("foregroundService")) {
                if (runningServiceInfo.foreground) {
                    serviceRunning = true;
                }
            }
        }
        return serviceRunning;
    }
}