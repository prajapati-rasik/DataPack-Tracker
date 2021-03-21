package Usage_calculation.asyncTasks;

import android.annotation.SuppressLint;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import Usage_calculation.adapters.allAppUsageFBAdapter;
import Usage_calculation.makeCalenderInstances;
import Usage_calculation.models.ForegroundBackgroundModel;
import Usage_calculation.models.applicationUIDModel;
import Usage_calculation.models.dailyUsageModel;
import Usage_calculation.resultFormatter;

public class allAppUsageAsyncFB extends AsyncTask<Integer, Integer, List<applicationUIDModel>> {

    private static NetworkStats networkStats;
    private final WeakReference<Context> contextWeakReference;
    private final WeakReference<ProgressBar> progressBarWeakReference;
    private final WeakReference<RecyclerView> recyclerViewWeakReference;
    private final WeakReference<RelativeLayout> relativeLayoutWeakReference;
    private int networkType;
    private int durationType;
    private SharedPreferences sharedPreferences;
    private boolean isDualSim;
    private String id1;
    private String id2;
    private List<applicationUIDModel> k;
    private List<applicationUIDModel> l;
    private List<ApplicationInfo> m;
    private long n;
    private Hashtable<Integer, ForegroundBackgroundModel> p;
    private List<applicationUIDModel> q;

    public allAppUsageAsyncFB(Context context, ProgressBar progressBar, RecyclerView recyclerView, int i2, int i3, RelativeLayout relativeLayout) {
        this.contextWeakReference = new WeakReference<>(context);
        this.progressBarWeakReference = new WeakReference<>(progressBar);
        this.relativeLayoutWeakReference = new WeakReference<>(relativeLayout);
        this.recyclerViewWeakReference = new WeakReference<>(recyclerView);
        this.networkType = i2;
        this.durationType = i3;
    }

    @RequiresApi(api = 23)
    @SuppressLint({"WrongConstant"})
    private void a(int i2, int i3) throws RemoteException {
        dailyUsageModel dailyUsageModel;
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) this.contextWeakReference.get().getSystemService(Context.NETWORK_STATS_SERVICE);
        if (networkStatsManager != null) {
            switch (i2) {
                case 0:
                    dailyUsageModel = makeCalenderInstances.lastHour(this.contextWeakReference.get());
                    break;
                case 1:
                    dailyUsageModel = makeCalenderInstances.last12Hours(this.contextWeakReference.get());
                    break;
                case 2:
                    dailyUsageModel = makeCalenderInstances.today(this.contextWeakReference.get());
                    break;
                case 3:
                    dailyUsageModel = makeCalenderInstances.yesterday(this.contextWeakReference.get());
                    break;
                case 4:
                    dailyUsageModel = makeCalenderInstances.weekly(this.contextWeakReference.get());
                    break;
                case 5:
                    dailyUsageModel = makeCalenderInstances.monthly(this.contextWeakReference.get());
                    break;
                case 6:
                    dailyUsageModel = makeCalenderInstances.yearly(this.contextWeakReference.get());
                    break;
                default:
                    dailyUsageModel = makeCalenderInstances.lastHour(this.contextWeakReference.get());
                    break;
            }
            if (i3 == 0) {
                networkStats = networkStatsManager.querySummary(0, this.id1, dailyUsageModel.getStart().getTimeInMillis(), dailyUsageModel.getEnd().getTimeInMillis());
            } else if (i3 == 1) {
                networkStats = networkStatsManager.querySummary(0, this.id2, dailyUsageModel.getStart().getTimeInMillis(), dailyUsageModel.getEnd().getTimeInMillis());
            } else if (i3 != 2) {
                networkStats = networkStatsManager.querySummary(0, this.id1, dailyUsageModel.getStart().getTimeInMillis(), dailyUsageModel.getEnd().getTimeInMillis());
            } else {
                networkStats = networkStatsManager.querySummary(1, null, dailyUsageModel.getStart().getTimeInMillis(), dailyUsageModel.getEnd().getTimeInMillis());
            }
        }
        this.p = new Hashtable<>();
        if (networkStats != null) {
            NetworkStats.Bucket bucket = new NetworkStats.Bucket();
            while (networkStats.hasNextBucket()) {
                networkStats.getNextBucket(bucket);
                if (this.p.containsKey(bucket.getUid())) {
                    ForegroundBackgroundModel ForegroundBackgroundModel1 = this.p.get(bucket.getUid());
                    if (bucket.getState() == 1) {
                        ForegroundBackgroundModel1.setBackgroundRx(ForegroundBackgroundModel1.getBackgroundRx() + bucket.getRxBytes());
                        ForegroundBackgroundModel1.setBackgroundTx(ForegroundBackgroundModel1.getBackgroundTx() + bucket.getTxBytes());
                    } else if (bucket.getState() == 2) {
                        ForegroundBackgroundModel1.setForegroundRx(ForegroundBackgroundModel1.getForegroundRx() + bucket.getRxBytes());
                        ForegroundBackgroundModel1.setForegroundTx(ForegroundBackgroundModel1.getForegroundTx() + bucket.getTxBytes());
                    }
                    this.p.put(bucket.getUid(), ForegroundBackgroundModel1);
                } else {
                    ForegroundBackgroundModel ForegroundBackgroundModel2 = new ForegroundBackgroundModel();
                    if (bucket.getState() == 1) {
                        ForegroundBackgroundModel2.setBackgroundRx(bucket.getRxBytes());
                        ForegroundBackgroundModel2.setBackgroundTx(bucket.getTxBytes());
                    } else if (bucket.getState() == 2) {
                        ForegroundBackgroundModel2.setForegroundRx(bucket.getRxBytes());
                        ForegroundBackgroundModel2.setForegroundTx(bucket.getTxBytes());
                    }
                    this.p.put(bucket.getUid(), ForegroundBackgroundModel2);
                }
            }
        }
        NetworkStats networkStats1 = networkStats;
        if (networkStats1 != null) {
            networkStats1.close();
        }
        PackageManager packageManager = this.contextWeakReference.get().getPackageManager();
        List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        ArrayList<ApplicationInfo> arrayList = new ArrayList<>();
        for (ApplicationInfo applicationInfo : installedApplications) {
            if ((applicationInfo.flags & 1) == 0 || (applicationInfo.flags & 128) != 0) {
                arrayList.add(applicationInfo);
            }
        }
        this.q = new ArrayList<>();
        if (arrayList.size() > 0) {
            for (ApplicationInfo applicationInfo2 : arrayList) {
                if (this.p.containsKey(applicationInfo2.uid)) {
                    applicationUIDModel applicationUIDModel = new applicationUIDModel();
                    applicationUIDModel.setLabel(applicationInfo2.loadLabel(packageManager).toString());
                    applicationUIDModel.setUid(applicationInfo2.uid);
                    applicationUIDModel.setPackageName(applicationInfo2.packageName);
                    applicationUIDModel.setUri(Uri.parse("android.resource://" + applicationInfo2.packageName + "/" + applicationInfo2.icon));
                    applicationUIDModel.setBackgroundData(resultFormatter.formatVal((double) (this.p.get(applicationInfo2.uid).getBackgroundRx() + this.p.get(applicationInfo2.uid).getBackgroundTx())));
                    applicationUIDModel.setForegroundData(resultFormatter.formatVal((double) (this.p.get(applicationInfo2.uid).getForegroundRx() + this.p.get(applicationInfo2.uid).getForegroundTx())));
                    applicationUIDModel.setData(this.p.get(applicationInfo2.uid).getForegroundTx() + this.p.get(applicationInfo2.uid).getForegroundRx() + this.p.get(applicationInfo2.uid).getBackgroundTx() + this.p.get(applicationInfo2.uid).getBackgroundRx());
                    this.n = this.n + applicationUIDModel.getData();
                    this.q.add(applicationUIDModel);
                }
            }
        }
        List<applicationUIDModel> list = this.q;
        if (list != null && list.size() > 0) {
            Collections.sort(this.q, new Comparator<applicationUIDModel>() {

                public int a(applicationUIDModel applicationUIDModel1, applicationUIDModel applicationUIDModel2) {
                    return Long.compare(applicationUIDModel2.getData(), applicationUIDModel1.getData());
                }

                public int compare(applicationUIDModel applicationUIDModel, applicationUIDModel applicationUIDModel2) {
                    return a(applicationUIDModel, applicationUIDModel2);
                }
            });
            this.l = new ArrayList<>();
            long i4 = this.q.get(0).getData();
            for (applicationUIDModel applicationUIDModel2 : this.q) {
                int i5 = this.n != 0 ? (int) ((applicationUIDModel2.getData() * 100) / this.n) : 0;
                if (applicationUIDModel2.getData() == 0) {
                    applicationUIDModel2.setPercentage("0 %");
                } else if (i5 < 1) {
                    applicationUIDModel2.setPercent(1);
                    applicationUIDModel2.setPercentage("< 1 %");
                } else {
                    if (i4 != 0) {
                        applicationUIDModel2.setPercent((int) ((applicationUIDModel2.getData() * 100) / i4));
                    }
                    applicationUIDModel2.setPercentage(i5 + " %");
                }
                this.l.add(applicationUIDModel2);
            }
        }
    }

    private void b(int i2, int i3) {
        try {
            a(i2, i3);
        } catch (Exception ignored) {
        }
    }

    public List<applicationUIDModel> doInBackground(Integer... numArr) {
        try {
            List<ApplicationInfo> installedApplications = this.contextWeakReference.get().getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
            this.m = new ArrayList<>();
            for (ApplicationInfo applicationInfo : installedApplications) {
                if ((applicationInfo.flags & 1) == 0 || (applicationInfo.flags & 128) != 0) {
                    this.m.add(applicationInfo);
                }
            }
            this.k = new ArrayList<>();
            b(this.networkType, this.durationType);
            return this.l;
        } catch (Exception unused) {
            return this.l;
        }
    }

    public void onPostExecute(List<applicationUIDModel> list) {
        try {
            if (this.contextWeakReference.get().getApplicationContext() != null) {
                this.progressBarWeakReference.get().setVisibility(View.INVISIBLE);
                if (list != null && list.size() > 0) {
                    this.relativeLayoutWeakReference.get().setVisibility(View.INVISIBLE);
                    this.recyclerViewWeakReference.get().setVisibility(View.VISIBLE);
                    this.recyclerViewWeakReference.get().setAdapter(new allAppUsageFBAdapter(this.contextWeakReference.get(), list));
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.contextWeakReference.get());
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    this.recyclerViewWeakReference.get().setLayoutManager(linearLayoutManager);
                    this.recyclerViewWeakReference.get().setItemAnimator(new DefaultItemAnimator());
                    this.progressBarWeakReference.get().setVisibility(View.INVISIBLE);
                } else {
                    this.progressBarWeakReference.get().setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception unused) {
            super.onPostExecute(list);
        }
    }

    public void onPreExecute() {
        try {
            this.relativeLayoutWeakReference.get().setVisibility(View.INVISIBLE);
            this.sharedPreferences = this.contextWeakReference.get().getSharedPreferences("MultipleSim", Context.MODE_PRIVATE);
            this.isDualSim = this.sharedPreferences.getBoolean("isDualSim", false);
            this.id1 = this.sharedPreferences.getString("subscriberId1", null);
            this.id2 = this.sharedPreferences.getString("subscriberId2", null);
            this.recyclerViewWeakReference.get().setVisibility(View.INVISIBLE);
            this.progressBarWeakReference.get().setVisibility(View.VISIBLE);
        } catch (Exception unused) {
            super.onPreExecute();
        }
    }
}
