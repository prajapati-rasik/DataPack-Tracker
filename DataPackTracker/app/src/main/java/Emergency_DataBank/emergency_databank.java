package Emergency_DataBank;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.datapacktracker.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import Usage_calculation.PermissionChecker;
import Usage_calculation.SubscriberDetails;

public class emergency_databank extends AppCompatActivity {

    ProgressBar timeProgressBar;
    ProgressBar dataProgressBar;
    TextInputEditText timeLimit;
    TextInputEditText dataLimit;
    TextView timeBox;
    TextView dataBox;
    Button enabler;
    Calendar cal;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Thread thread;
    boolean isrunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_databank);

        checkPermissions();
        SubscriberDetails.setAllSubscriptionDetails(this, this);
        sharedPreferences = this.getSharedPreferences("Emergency_Databank", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        computeDataThread.setContext(this);

        timeLimit = findViewById(R.id.time_limit);
        dataLimit = findViewById(R.id.data_limit);
        enabler = findViewById(R.id.enable_databank);
        timeBox = findViewById(R.id.show_time);
        dataBox = findViewById(R.id.show_data);
        timeProgressBar = findViewById(R.id.timeProgressBar);
        dataProgressBar = findViewById(R.id.dataProgressBar);

        isrunning = sharedPreferences.getBoolean("isrunning", false);
        String data_value = sharedPreferences.getString("data_value", null);
        String time_value = sharedPreferences.getString("time_value", null);
        if(isrunning){
            timeLimit.setVisibility(View.GONE);
            dataLimit.setVisibility(View.GONE);
            timeBox.setVisibility(View.VISIBLE);
            dataBox.setVisibility(View.VISIBLE);
            dataBox.setText(data_value);
            timeBox.setText(time_value);
            enabler.setText(R.string.cancel);
        }

        timeLimit.setOnClickListener(v -> {
            cal = Calendar.getInstance();
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(emergency_databank.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    timeBox.setText(selectedHour + ":" + selectedMinute);
                    timeLimit.setText(selectedHour + ":" + selectedMinute);
                    cal.set(Calendar.HOUR_OF_DAY, selectedHour);
                    cal.set(Calendar.MINUTE, selectedMinute);
                }
            }, hour, minute, true);
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        });

        sharedPreferences.registerOnSharedPreferenceChangeListener((sharedPreferences, key) -> {
            if(!sharedPreferences.getBoolean("isrunning", false)){
                enabler.performClick();
            }
        }
        );

        enabler.setOnClickListener(v -> {

            String isCancelledText = enabler.getText().toString();
            if(isCancelledText.equals("Cancel")){
                computeDataThread.setCancelled();
                enabler.setText(R.string.enableDatabank);
                timeLimit.getText().clear();
                dataLimit.getText().clear();
                timeLimit.setVisibility(View.VISIBLE);
                dataLimit.setVisibility(View.VISIBLE);
                timeBox.setVisibility(View.GONE);
                dataBox.setVisibility(View.GONE);
            }else {
                String dataString = Objects.requireNonNull(dataLimit.getText()).toString();
                timeLimit.setVisibility(View.GONE);
                dataLimit.setVisibility(View.GONE);
                timeBox.setVisibility(View.VISIBLE);
                dataBox.setVisibility(View.VISIBLE);
                dataBox.setText(dataString);
                enabler.setText(R.string.cancel);

                editor.putBoolean("isrunning", true);
                editor.putString("data_value", dataString);
                editor.putString("time_value", timeLimit.getText().toString());

                long datalim = Long.parseLong(dataLimit.getText().toString()) * 1048576;

                thread = new computeDataThread(datalim, cal.getTimeInMillis());
                thread.start();

                editor.apply();
            }
        });
    }

    private boolean checkPermissions() {
        if (!PermissionChecker.checkPhoneStatePermission(this, this)) {
            return false;
        } else {
            if(!PermissionChecker.checkUsageAccessPermission(this)){
                PermissionChecker.checkUsageAccessPermissionWithWatchingMode(this, this);
            }
        }
        return PermissionChecker.checkUsageAccessPermission(this);
    }
}
