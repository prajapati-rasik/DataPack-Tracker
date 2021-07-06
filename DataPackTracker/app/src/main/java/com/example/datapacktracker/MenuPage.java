package com.example.datapacktracker;

import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Emergency_DataBank.emergency_databank;
import Profile.profile;
import Usage_calculation.PermissionChecker;
import Usage_calculation.SubscriberDetails;
import Usage_calculation.asyncTasks.allAppUsageAsync;
import Usage_calculation.getNetStats;
import Usage_calculation.makeCalenderInstances;
import Usage_calculation.models.dailyUsageModel;
import Usage_calculation.models.dataUsageAllStringModel;
import Usage_calculation.models.dataUsageIntervalTypeModel;
import Usage_calculation.models.singleSimModel;
import Usage_calculation.usage_calculation;
import appUsage.allAppUsage;
import bedTimeMode.bedTimeMode;
import home.HomeActivity;
import speedTesting.speedTesting;
import towerNavigation.findTower;
import user_registration.login_page;

public class MenuPage extends AppCompatActivity {

    DrawerLayout drawerLayout;
    TextView textData, textWifi, textUpload, textDownload;
    LineChart lineChart;
    private static final int REQUEST_CALL =1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_page);

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivity(new Intent(MenuPage.this, HomeActivity.class));
            finish();
        }

        checkPermissions();
        SubscriberDetails.setAllSubscriptionDetails(this, this);

        drawerLayout = findViewById(R.id.drawer_layout);
        ImageView drawer = findViewById(R.id.drawer);

        drawer.setOnClickListener(v -> openDrawer(drawerLayout));

        //View databank = findViewById(R.id.databank_menu);
        View towerFinder = findViewById(R.id.find_tower_menu);
        View bed_time_mode = findViewById(R.id.bed_time_mod_menu);
        View network_speed = findViewById(R.id.network_speed_menu);
        View Profile = findViewById(R.id.profile_menu);
        View data_calculation = findViewById(R.id.data_usage_menu);

        this.lineChart = findViewById(R.id.linechart);
        this.textData = findViewById(R.id.DataLayoutMainText);
        this.textWifi = findViewById(R.id.WifiLayoutMainText);
        this.textUpload = findViewById(R.id.UploadLayoutMainText);
        this.textDownload = findViewById(R.id.downloadLayoutMainText);

        final View settings = findViewById(R.id.settings_icon);

        /*databank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuPage.this, emergency_databank.class));
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });*/

        towerFinder.setOnClickListener(v -> {
            startActivity(new Intent(MenuPage.this, findTower.class));
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        bed_time_mode.setOnClickListener(v -> {
            startActivity(new Intent(MenuPage.this, bedTimeMode.class));
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        network_speed.setOnClickListener(v -> {
            startActivity(new Intent(MenuPage.this, speedTesting.class));
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        Profile.setOnClickListener(v -> {
            startActivity(new Intent(MenuPage.this, profile.class));
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        data_calculation.setOnClickListener(v -> {
            startActivity(new Intent(MenuPage.this, usage_calculation.class));
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        settings.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(MenuPage.this, settings);
            popup.getMenuInflater().inflate(R.menu.setting, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.logout_button_menu) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MenuPage.this, HomeActivity.class));
                    finish();
                } else if (item.getItemId() == R.id.change_password) {
                    resetPassword();
                } else {
                    SharedPreferences sharedPreferences = this.getSharedPreferences("MultipleSim", Context.MODE_PRIVATE);
                    String name1 = sharedPreferences.getString("simCarrier1", null);
                    String name2 = sharedPreferences.getString("simCarrier2", null);
                    SubMenu submenu = item.getSubMenu();
                    if(name1 != null || name2 != null) submenu.clear();
                    if(name1 != null) submenu.add(name1).setOnMenuItemClickListener(item1 -> {
                        makeCall((String) item1.getTitle());
                        return true;
                    });
                    if(name2 != null) submenu.add(name2).setOnMenuItemClickListener(item1 -> {
                        makeCall((String) item1.getTitle());
                        return true;
                    });
                }
                return true;
            });
            popup.show();
        });

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
        try {
            if (valueOf) {
                singleSimModel wifi = getNetStats.getDataWifi(todayStart, todayEnd, networkStatsManager);
                singleSimModel sim1Data = getNetStats.getDataMobile(todayStart, todayEnd, id1, networkStatsManager);
                singleSimModel sim2Data = getNetStats.getDataMobile(todayStart, todayEnd, id2, networkStatsManager);
                long total = sim1Data.getTx() + sim1Data.getRx() + sim2Data.getRx() + sim2Data.getTx();
                double total1 = (double) total / 1048576.0d;
                String totString = makeString(total1) + " MB";
                this.textData.setText(totString);
                total = wifi.getRx() + wifi.getTx();
                total1 = (double) total / 1048576.0d;
                totString = makeString(total1) + " MB";
                this.textWifi.setText(totString);
                total = sim1Data.getRx() + sim2Data.getRx();
                total1 = (double) total / 1048576.0d;
                totString = makeString(total1) + " MB";
                this.textDownload.setText(totString);
                total = sim1Data.getTx() + sim2Data.getTx();
                total1 = (double) total / 1048576.0d;
                totString = makeString(total1) + " MB";
                this.textUpload.setText(totString);
            } else {
                singleSimModel wifi = getNetStats.getDataWifi(todayStart, todayEnd, networkStatsManager);
                singleSimModel sim1Data = getNetStats.getDataMobile(todayStart, todayEnd, id1, networkStatsManager);
                long total = sim1Data.getTx() + sim1Data.getRx();
                double total1 = (double) total / 1048576.0d;
                String totString = makeString(total1) + " MB";
                this.textData.setText(totString);
                total = wifi.getRx() + wifi.getTx();
                total1 = (double) total / 1048576.0d;
                totString = makeString(total1) + " MB";
                this.textWifi.setText(totString);
                total = sim1Data.getRx();
                total1 = (double) total / 1048576.0d;
                totString = makeString(total1) + " MB";
                this.textDownload.setText(totString);
                total = sim1Data.getTx();
                total1 = (double) total / 1048576.0d;
                totString = makeString(total1) + " MB";
                this.textUpload.setText(totString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setGraph();
    }

    private void setGraph() {
        this.lineChart.setTouchEnabled(true);
        this.lineChart.setPinchZoom(true);
        this.lineChart.setNoDataText("No Data Found");
        this.lineChart.setDrawGridBackground(false);
        this.lineChart.setDrawBorders(false);
        ArrayList<Entry> values = new ArrayList<>();
        List<dailyUsageModel> weeklist = makeCalenderInstances.days(7, this);
        SharedPreferences sharedPreferences = this.getSharedPreferences("MultipleSim", Context.MODE_PRIVATE);
        boolean valueOf = sharedPreferences.getBoolean("isDualSim", false);
        String id1 = sharedPreferences.getString("subscriberId1", null);
        String id2 = sharedPreferences.getString("subscriberId2", null);
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) this.getSystemService(Context.NETWORK_STATS_SERVICE);
        int i = 1;
        try {
            if (valueOf) {
                while (i <= 7) {
                    i--;
                    singleSimModel wifi = getNetStats.getDataWifi(weeklist.get(i).getStart(), weeklist.get(i).getEnd(), networkStatsManager);
                    singleSimModel sim1Data = getNetStats.getDataMobile(weeklist.get(i).getStart(), weeklist.get(i).getEnd(), id1, networkStatsManager);
                    singleSimModel sim2Data = getNetStats.getDataMobile(weeklist.get(i).getStart(), weeklist.get(i).getEnd(), id2, networkStatsManager);
                    long total = wifi.getRx() + wifi.getTx() + sim1Data.getTx() + sim1Data.getRx() + sim2Data.getRx() + sim2Data.getTx();
                    float total1 = (float) total / 1048576.0f;
                    values.add(new Entry(i, total1));
                    i += 2;
                }
            } else {
                while (i <= 7) {
                    i--;
                    singleSimModel wifi = getNetStats.getDataWifi(weeklist.get(i).getStart(), weeklist.get(i).getEnd(), networkStatsManager);
                    singleSimModel sim1Data = getNetStats.getDataMobile(weeklist.get(i).getStart(), weeklist.get(i).getEnd(), id1, networkStatsManager);
                    long total = wifi.getRx() + wifi.getTx() + sim1Data.getTx() + sim1Data.getRx();
                    float total1 = (float) total / 1048576.0f;
                    values.add(new Entry(i, total1));
                    i += 2;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        LineDataSet set1;
        if (this.lineChart.getData() != null && this.lineChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) this.lineChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            this.lineChart.getData().notifyDataChanged();
            this.lineChart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(values, "Used data in last 7 days (MB)");
            set1.setDrawValues(false);
            set1.setDrawIcons(false);
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.DKGRAY);
            set1.setCircleColor(Color.DKGRAY);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            this.lineChart.setData(data);
        }
    }

    private static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    private boolean checkPermissions() {
        if (!PermissionChecker.checkPhoneStatePermission(this, this)) {
            return false;
        } else {
            if (!PermissionChecker.checkUsageAccessPermission(this)) {
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

    private void resetPassword() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        final View dialogView = inflater.inflate(R.layout.activity_reset_password, null);
        dialogBuilder.setView(dialogView);
        final TextInputEditText editEmail = dialogView.findViewById(R.id.edtTxtEmailInput);
        final Button btnReset = dialogView.findViewById(R.id.send_reset_pass);
        final ProgressBar progress = dialogView.findViewById(R.id.progressBar);
        final AlertDialog dialog = dialogBuilder.create();
        btnReset.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                return;
            }
            progress.setVisibility(View.VISIBLE);
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(MenuPage.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MenuPage.this, HomeActivity.class));
                            finish();
                        } else {
                            Toast.makeText(MenuPage.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                        }
                        progress.setVisibility(View.GONE);
                        dialog.dismiss();
                    });
        });
        dialog.show();
    }

    @Override
    public void onResume() {
        checkPermissions();
        SubscriberDetails.setAllSubscriptionDetails(this, this);
        super.onResume();
    }

    void makeCall(String num) {
        num = num.toLowerCase();
        if (ContextCompat.checkSelfPermission(MenuPage.this, "android.permission.CALL_PHONE") != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MenuPage.this, new String[]{("android.permission.CALL_PHONE")}, REQUEST_CALL);
        }
        if (num.equals("airtel")){
            String dial = "tel:121" ;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }else if(num.equals("jio") || num.equals("reliance") || num.equals("reliance jio") || num.equals("jio 4g")){
            String dial = "tel:18008899999";
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }else if(num.contains("vodaphone") || num.contains("vodafone") || num.contains("idea") || num.contains("vi") || num.contains("vi india-idea") || num.contains("vi 4g")){
            String dial = "tel:199";
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }else if(num.contains("bsnl")){
            String dial = "tel:1500";
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }else {
            Toast.makeText(MenuPage.this, "Number not available", Toast.LENGTH_SHORT).show();
        }
    }

}
