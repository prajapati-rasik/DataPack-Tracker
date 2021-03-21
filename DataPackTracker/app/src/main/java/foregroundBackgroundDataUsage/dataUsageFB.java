package foregroundBackgroundDataUsage;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.datapacktracker.R;

import java.util.Objects;

import Usage_calculation.PermissionChecker;
import Usage_calculation.SubscriberDetails;
import Usage_calculation.asyncTasks.allAppUsageAsyncFB;

public class dataUsageFB extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Spinner innerSpinner;
    private Spinner spinner;
    private ProgressBar progressBar;
    private RelativeLayout relativeLayout;
    private allAppUsageAsyncFB async;
    private SharedPreferences sharedPreferences;
    private Boolean isDualSim;
    private String id1;
    private String id2;
    private int y = 0;

    private void setData(int i, int i2) {
        try {
            this.async = new allAppUsageAsyncFB(getApplicationContext(), this.progressBar, this.recyclerView, i, i2, this.relativeLayout);
            this.async.execute();
        } catch (Exception ignored) {
        }
    }

    private void setView() {
        this.swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.foregroundBackgroundSwipeRefreshLayout);
        this.progressBar = (ProgressBar) findViewById(R.id.foregroundBackgroundProgressBar);
        this.recyclerView = (RecyclerView) findViewById(R.id.foregroundBackgroundRecycleView);
        this.innerSpinner = (Spinner) findViewById(R.id.foregroundBackgroundSpinnerInterval);
        this.spinner = (Spinner) findViewById(R.id.foregroundBackgroundSpinner);
        this.relativeLayout = (RelativeLayout) findViewById(R.id.noAppUsageLayoutFB);
    }

    private void setAdapters() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("MultipleSim", 0);
            this.sharedPreferences = sharedPreferences;
            this.isDualSim = sharedPreferences.getBoolean("isDualSim", false);
            String string = this.sharedPreferences.getString("simName1", "SIM 1");
            String string2 = this.sharedPreferences.getString("simName2", "SIM 2");
            this.id1 = this.sharedPreferences.getString("subscriberId1", "");
            this.id2 = this.sharedPreferences.getString("subscriberId2", "");
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, (int) android.R.layout.simple_spinner_item, this.isDualSim ? new String[]{string, string2, getString(R.string.wifi)} : new String[]{string, getString(R.string.wifi)});
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.spinner.setAdapter((SpinnerAdapter) arrayAdapter);
            this.spinner.setSelection(0);
            ArrayAdapter arrayAdapter1 = new ArrayAdapter(this, (int) android.R.layout.simple_spinner_item, new String[]{getResources().getString(R.string.last_hour), getResources().getString(R.string.last_twelve_hours), getResources().getString(R.string.today), getResources().getString(R.string.yesterday), getResources().getString(R.string.week), getResources().getString(R.string.month), getResources().getString(R.string.overall_time), getResources().getString(R.string.custom)});
            arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.innerSpinner.setAdapter((SpinnerAdapter) arrayAdapter1);
            this.innerSpinner.setSelection(0);

            this.innerSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (dataUsageFB.this.isDualSim) {
                        dataUsageFB appUsageActivity = dataUsageFB.this;
                        appUsageActivity.setData(position, appUsageActivity.spinner.getSelectedItemPosition());
                    } else if (dataUsageFB.this.spinner.getSelectedItemPosition() == 1) {
                        dataUsageFB.this.setData(position, 2);
                    } else {
                        dataUsageFB appUsageActivity2 = dataUsageFB.this;
                        appUsageActivity2.setData(position, appUsageActivity2.spinner.getSelectedItemPosition());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            this.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                    if (dataUsageFB.this.isDualSim) {
                        dataUsageFB appUsageActivity = dataUsageFB.this;
                        appUsageActivity.setData(appUsageActivity.innerSpinner.getSelectedItemPosition(), i);
                    } else if (i == 1) {
                        dataUsageFB appUsageActivity2 = dataUsageFB.this;
                        appUsageActivity2.setData(appUsageActivity2.innerSpinner.getSelectedItemPosition(), 2);
                    } else {
                        dataUsageFB appUsageActivity3 = dataUsageFB.this;
                        appUsageActivity3.setData(appUsageActivity3.innerSpinner.getSelectedItemPosition(), i);
                    }
                }
            });
            this.innerSpinner.setSelection(2);
        } catch (Exception ignored) {
        }
    }

    private void onRefreshing() {
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (dataUsageFB.this.isDualSim) {
                    dataUsageFB appUsageActivity = dataUsageFB.this;
                    appUsageActivity.setData(appUsageActivity.innerSpinner.getSelectedItemPosition(), dataUsageFB.this.spinner.getSelectedItemPosition());
                } else if (dataUsageFB.this.spinner.getSelectedItemPosition() == 1) {
                    dataUsageFB appUsageActivity2 = dataUsageFB.this;
                    appUsageActivity2.setData(appUsageActivity2.innerSpinner.getSelectedItemPosition(), 2);
                } else {
                    dataUsageFB appUsageActivity3 = dataUsageFB.this;
                    appUsageActivity3.setData(appUsageActivity3.innerSpinner.getSelectedItemPosition(), dataUsageFB.this.spinner.getSelectedItemPosition());
                }
                dataUsageFB.this.swipeRefreshLayout.setRefreshing(false);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.foreground_background_data_usage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.foregroundBackgroundToolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        checkPermissions();
        SubscriberDetails.setAllSubscriptionDetails(this, this);
        setView();
        onRefreshing();
        setAdapters();
    }

    @Override
    public void onDestroy() {
        allAppUsageAsyncFB allAppUsageAsync1 = this.async;
        if (allAppUsageAsync1 != null) {
            allAppUsageAsync1.cancel(true);
            this.async = null;
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {
        checkPermissions();
        SubscriberDetails.setAllSubscriptionDetails(this, this);
        super.onResume();
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        onBackPressed();
        return true;

    }
}
