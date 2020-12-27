package Emergency_DataBank;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.datapacktracker.R;

import java.util.Calendar;

public class emergency_databank extends AppCompatActivity {

    ProgressBar timeProgressBar;
    ProgressBar dataProgressBar;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_databank);

        final String MyPREFERENCES = "Emergency_DataBank" ;
        final String DATA_LIMIT = "Data_Limit";
        final String TIME_LIMIT = "Time_Limit";

        final EditText time_limit = findViewById(R.id.time_limit);
        final EditText data_limit = findViewById(R.id.data_limit);
        final TextView show_time = findViewById(R.id.show_time);
        final TextView show_data = findViewById(R.id.show_data);
        final Button databank = findViewById(R.id.enable_databank);
        timeProgressBar = findViewById(R.id.timeProgressBar);
        dataProgressBar = findViewById(R.id.dataProgressBar);
        final TimePickerDialog[] picker = new TimePickerDialog[1];
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();

        time_limit.setInputType(InputType.TYPE_NULL);
        time_limit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker[0] = new TimePickerDialog(emergency_databank.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                time_limit.setText(sHour + ":" + sMinute);
                                show_time.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                picker[0].show();
            }
        });

        databank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(!computeDataThread.getStopThread()){
                    computeDataThread.setStopThread(true);
                    timeProgressBar.setVisibility(View.GONE);
                    dataProgressBar.setVisibility(View.GONE);
                    data_limit.setVisibility(View.VISIBLE);
                    time_limit.setVisibility(View.VISIBLE);
                    editor.clear();
                }else{
                    computeDataThread backgroundThread = new computeDataThread(getApplicationContext(),editor);
                    computeDataThread.setStopThread(false);
                    backgroundThread.run();
                    String s = String.valueOf(computeDataThread.getUsedData());
                    show_data.setText(s);
                    data_limit.setVisibility(View.GONE);
                    time_limit.setVisibility(View.GONE);
                    timeProgressBar.setVisibility(View.VISIBLE);
                    dataProgressBar.setVisibility(View.VISIBLE);

                    updateUI backgroundUpdateUi = new updateUI(timeProgressBar,dataProgressBar,sharedpreferences);
                    backgroundUpdateUi.run();
                }
                if(!computeDataThread.getStopThread()){
                    databank.setText("Disable databank");
                }else{
                    databank.setText("enable databank");
                }
            }*/
                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(emergency_databank.this)
                                .setSmallIcon(R.drawable.ic_baseline_data_usage_24)
                                .setContentTitle("Emergency Databank")
                                .setContentText("Data Limit Exceeded");

                Intent notificationIntent = new Intent(emergency_databank.this, emergency_databank.class);
                PendingIntent contentIntent = PendingIntent.getActivity(emergency_databank.this, 0, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(contentIntent);

                // Add as notification
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                assert manager != null;
                manager.notify(0, builder.build());
            }
        });

    }

    /*@Override
    protected void onStart() {
        super.onStart();
        if(!checkForPermission(this)){
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }
        if(!computeDataThread.getStopThread()) {
            updateUI backgroundUpdateUi = new updateUI(timeProgressBar,dataProgressBar,sharedpreferences);
            backgroundUpdateUi.run();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateUI.setRunning(false);
    }

    private boolean checkForPermission(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        assert appOps != null;
        int mode = appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), context.getPackageName());
        return mode == MODE_ALLOWED;
    }*/
}
