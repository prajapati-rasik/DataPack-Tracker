package hotspotUsage;

import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.datapacktracker.R;

import java.lang.ref.WeakReference;

import Usage_calculation.PermissionChecker;
import Usage_calculation.SubscriberDetails;
import Usage_calculation.makeCalenderInstances;
import Usage_calculation.models.dailyDataUsageResultModel;
import Usage_calculation.models.dailyUsageModel;
import Usage_calculation.resultFormatter;

public class hotspotUsage extends AppCompatActivity {

    TextView received;
    TextView sent;
    TextView total;
    private Spinner spinner;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    private static class hotspotAsync extends AsyncTask<Void, Void, dailyDataUsageResultModel> {
        static final boolean a = (!hotspotUsage.class.desiredAssertionStatus());
        private final WeakReference<Context> contextWeakReference;
        private final WeakReference<ProgressBar> progressBarWeakReference;
        private WeakReference<TextView> received;
        private WeakReference<TextView> sent;
        private WeakReference<TextView> total;
        private int intervalType;

        hotspotAsync(Context context, TextView textView, TextView textView2, TextView textView3, ProgressBar progressBar, int i) {
            this.contextWeakReference = new WeakReference<>(context);
            this.received = new WeakReference<>(textView);
            this.sent = new WeakReference<>(textView2);
            this.total = new WeakReference<>(textView3);
            this.progressBarWeakReference = new WeakReference<>(progressBar);
            this.intervalType = i;
        }

        public dailyDataUsageResultModel doInBackground(Void... voidArr) {
            long j5 = 0;
            long j9 = 0;
            long j7 = 0;
            long j8 = 0;
            dailyDataUsageResultModel dailyDataUsageResultModel1 = new dailyDataUsageResultModel();
            try {
                SharedPreferences sharedPreferences = this.contextWeakReference.get().getSharedPreferences("MultipleSim", 0);
                boolean isDualSim = sharedPreferences.getBoolean("isDualSim", false);
                NetworkStats networkStats = null;
                NetworkStats querySummary = null;
                String string = sharedPreferences.getString("subscriberId1", null);
                String string2 = sharedPreferences.getString("subscriberId2", null);
                NetworkStatsManager networkStatsManager = (NetworkStatsManager) this.contextWeakReference.get().getSystemService(Context.NETWORK_STATS_SERVICE);
                dailyUsageModel dailyUsageModel1 = new dailyUsageModel();
                switch (this.intervalType) {
                    case 0:
                        dailyUsageModel1 = makeCalenderInstances.lastHour(this.contextWeakReference.get());
                        break;
                    case 1:
                        dailyUsageModel1 = makeCalenderInstances.last12Hours(this.contextWeakReference.get());
                        break;
                    case 2:
                        dailyUsageModel1 = makeCalenderInstances.today(this.contextWeakReference.get());
                        break;
                    case 3:
                        dailyUsageModel1 = makeCalenderInstances.yesterday(this.contextWeakReference.get());
                        break;
                    case 4:
                        dailyUsageModel1 = makeCalenderInstances.weekly(this.contextWeakReference.get());
                        break;
                    case 5:
                        dailyUsageModel1 = makeCalenderInstances.monthly(this.contextWeakReference.get());
                        break;
                    case 6:
                        dailyUsageModel1 = makeCalenderInstances.yearly(this.contextWeakReference.get());
                        break;
                }
                if (isDualSim) {
                    if (a || networkStatsManager != null) {
                        try {
                            networkStats = networkStatsManager.querySummary(0, string, dailyUsageModel1.getStart().getTimeInMillis(), dailyUsageModel1.getEnd().getTimeInMillis());
                            querySummary = networkStatsManager.querySummary(0, string2, dailyUsageModel1.getStart().getTimeInMillis(), dailyUsageModel1.getEnd().getTimeInMillis());
                            if (networkStats != null) {
                                NetworkStats.Bucket bucket = new NetworkStats.Bucket();
                                while (networkStats.hasNextBucket()) {
                                    networkStats.getNextBucket(bucket);
                                    if (bucket.getUid() == -5) {
                                        j7 += bucket.getTxBytes();
                                        j8 += bucket.getRxBytes();
                                    }
                                }
                            }
                            if (querySummary != null) {
                                NetworkStats.Bucket bucket2 = new NetworkStats.Bucket();
                                while (querySummary.hasNextBucket()) {
                                    querySummary.getNextBucket(bucket2);
                                    if (bucket2.getUid() == -5) {
                                        j7 += bucket2.getTxBytes();
                                        j8 += bucket2.getRxBytes();
                                    }
                                }
                            }
                            if (networkStats != null) {
                                networkStats.close();
                            }
                            if (querySummary != null) {
                                querySummary.close();
                            }
                            dailyDataUsageResultModel1.setReceiveBytes(j8);
                            dailyDataUsageResultModel1.setSendBytes(j7);
                            return dailyDataUsageResultModel1;
                        } catch (RemoteException e6) {
                            e6.printStackTrace();
                            dailyDataUsageResultModel1.setReceiveBytes(0);
                            dailyDataUsageResultModel1.setSendBytes(0);
                            return dailyDataUsageResultModel1;
                        }
                    } else {
                        throw new AssertionError();
                    }
                } else {
                    try {
                        if (a || networkStatsManager != null) {
                            networkStats = networkStatsManager.querySummary(0, string, dailyUsageModel1.getStart().getTimeInMillis(), dailyUsageModel1.getEnd().getTimeInMillis());
                            NetworkStats.Bucket bucket3 = new NetworkStats.Bucket();
                            while (networkStats.hasNextBucket()) {
                                networkStats.getNextBucket(bucket3);
                                if (bucket3.getUid() == -5) {
                                    j9 += bucket3.getTxBytes();
                                    j5 += bucket3.getRxBytes();
                                }
                            }
                            if (networkStats != null) {
                                networkStats.close();
                            }
                            if (networkStats != null) {
                                networkStats.close();
                            }
                            dailyDataUsageResultModel1.setReceiveBytes(j5);
                            dailyDataUsageResultModel1.setSendBytes(j9);
                            return dailyDataUsageResultModel1;
                        } else {
                            throw new AssertionError();
                        }
                    } catch (RemoteException e16) {
                        e16.printStackTrace();
                        dailyDataUsageResultModel1.setReceiveBytes(0);
                        dailyDataUsageResultModel1.setSendBytes(0);
                        return dailyDataUsageResultModel1;
                    }
                }
            } catch (Exception unused) {
                return dailyDataUsageResultModel1;
            }
        }

        public void onPostExecute(dailyDataUsageResultModel dailyDataUsageResultModel) {
            try {
                if (!(this.progressBarWeakReference.get() == null || this.received.get() == null || this.sent.get() == null || this.total.get() == null)) {
                    this.progressBarWeakReference.get().setVisibility(View.INVISIBLE);
                    this.received.get().setText(resultFormatter.formatVal((double) dailyDataUsageResultModel.getReceiveBytes()));
                    this.sent.get().setText(resultFormatter.formatVal((double) dailyDataUsageResultModel.getSendBytes()));
                    this.total.get().setText(resultFormatter.formatVal((double) (dailyDataUsageResultModel.getReceiveBytes() + dailyDataUsageResultModel.getSendBytes())));
                }
            } catch (Exception ignored) {
            }
            super.onPostExecute(dailyDataUsageResultModel);
        }

        public void onPreExecute() {
            this.progressBarWeakReference.get().setVisibility(View.VISIBLE);
            super.onPreExecute();
        }
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

    private void l() {
        try {
            this.received = (TextView) findViewById(R.id.txtReceivedHT);
            this.sent = (TextView) findViewById(R.id.txtSentHT);
            this.total = (TextView) findViewById(R.id.txtTotalHT);
            this.spinner = (Spinner) findViewById(R.id.spinnerHT);
            this.progressBar = (ProgressBar) findViewById(R.id.dataUsageMProgressBarHT);
            this.swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.hotspotUsageSwipeRefreshLayout);
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, (int) android.R.layout.simple_spinner_item, new String[]{getResources().getString(R.string.last_hour), getResources().getString(R.string.last_twelve_hours), getResources().getString(R.string.today), getResources().getString(R.string.yesterday), getResources().getString(R.string.week), getResources().getString(R.string.month), getResources().getString(R.string.overall_time)});
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.spinner.setAdapter((SpinnerAdapter) arrayAdapter);
            this.spinner.setSelection(0);
            this.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                    new hotspotAsync(hotspotUsage.this.getApplicationContext(), hotspotUsage.this.received, hotspotUsage.this.sent, hotspotUsage.this.total, hotspotUsage.this.progressBar, i).execute();
                }
            });
            this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new hotspotAsync(hotspotUsage.this.getApplicationContext(), hotspotUsage.this.received, hotspotUsage.this.sent, hotspotUsage.this.total, hotspotUsage.this.progressBar, hotspotUsage.this.spinner.getSelectedItemPosition()).execute();
                    hotspotUsage.this.swipeRefreshLayout.setRefreshing(false);
                }
            });
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.hotspot_data_usage);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbarHT));
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
        checkPermissions();
        SubscriberDetails.setAllSubscriptionDetails(this, this);
        l();
        this.spinner.setSelection(2);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        onBackPressed();
        return true;
    }
}
