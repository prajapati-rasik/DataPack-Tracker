package Usage_calculation;

import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.datapacktracker.MenuPage;
import com.example.datapacktracker.R;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Calendar;

import Emergency_DataBank.emergency_databank;
import Usage_calculation.models.singleSimModel;
import appUsage.allAppUsage;
import dataUsage.dataUsage;
import foregroundBackgroundDataUsage.dataUsageFB;
import hotspotUsage.hotspotUsage;

public class usage_calculation extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usagecalculation);

        Button data_usage = findViewById(R.id.dataUsageLinearLayoutUsage);
        Button app_usage = findViewById(R.id.appUsageLinearLayoutUsage);
        Button hotspot_usage = findViewById(R.id.hotspotUsageLinearLayoutUsage);
        Button foregroundBackground_usage = findViewById(R.id.backgroundForegroundUsageLinearLayoutUsage);

        TextView dataUsed = findViewById(R.id.txtDataUsedUsage);

        ImageView backButton = findViewById(R.id.backUsageCalculate);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(usage_calculation.this, MenuPage.class));
            }
        });

        data_usage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(usage_calculation.this, dataUsage.class));
            }
        });

        app_usage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(usage_calculation.this, allAppUsage.class));
            }
        });

        hotspot_usage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(usage_calculation.this, hotspotUsage.class));
            }
        });

        foregroundBackground_usage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(usage_calculation.this, dataUsageFB.class));
            }
        });

        checkPermissions();
        SubscriberDetails.setAllSubscriptionDetails(this, this);

        SharedPreferences sharedPreferences = this.getSharedPreferences("MultipleSim", Context.MODE_PRIVATE);
        boolean valueOf = sharedPreferences.getBoolean("isDualSim", false);
        String id1 = sharedPreferences.getString("subscriberId1", null);
        String id2 = sharedPreferences.getString("subscriberId2", null);
        Calendar todayStart = Calendar.getInstance();
        Calendar todayEnd = Calendar.getInstance();
        todayStart.add(Calendar.DATE, -1);
        todayStart.set(Calendar.HOUR_OF_DAY, 23);
        todayStart.set(Calendar.MINUTE, 59);
        todayEnd.add(Calendar.DATE, 1);
        todayEnd.set(Calendar.HOUR_OF_DAY, 0);
        todayEnd.set(Calendar.MINUTE, 1);
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) this.getSystemService(Context.NETWORK_STATS_SERVICE);
        try{
            if(valueOf){
                singleSimModel wifi = getNetStats.getDataWifi(todayStart, todayEnd, networkStatsManager);
                singleSimModel sim1Data = getNetStats.getDataMobile(todayStart, todayEnd, id1, networkStatsManager);
                singleSimModel sim2Data = getNetStats.getDataMobile(todayStart, todayEnd, id2, networkStatsManager);
                long total = wifi.getRx() + wifi.getTx() + sim1Data.getTx() + sim1Data.getRx() + sim2Data.getRx() + sim2Data.getTx();
                double total1 = (double)total / 1048576.0d;
                String totString = makeString(total1);
                dataUsed.setText(totString);
            }else{
                singleSimModel wifi = getNetStats.getDataWifi(todayStart, todayEnd, networkStatsManager);
                singleSimModel sim1Data = getNetStats.getDataMobile(todayStart, todayEnd, id1, networkStatsManager);
                long total = wifi.getRx() + wifi.getTx() + sim1Data.getTx() + sim1Data.getRx();
                double total1 = (double)total / 1048576.0d;
                String totString = makeString(total1);
                dataUsed.setText(totString);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
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

    private static String makeString(double val) {
        DecimalFormat dateFormatter = new DecimalFormat("#.#");
        dateFormatter.setRoundingMode(RoundingMode.UP);
        return dateFormatter.format(val);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
