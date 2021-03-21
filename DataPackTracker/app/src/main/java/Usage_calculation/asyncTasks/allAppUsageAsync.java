package Usage_calculation.asyncTasks;

import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.util.SparseLongArray;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Usage_calculation.adapters.allAppUsageAdapter;
import Usage_calculation.makeCalenderInstances;
import Usage_calculation.models.applicationUIDModel;
import Usage_calculation.models.dailyUsageModel;
import Usage_calculation.resultFormatter;

public class allAppUsageAsync extends AsyncTask<Integer, Integer, List<applicationUIDModel>> {
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
    private List<applicationUIDModel> l2;
    private List<applicationUIDModel> l1;
    private applicationUIDModel m;
    private long n;
    private List<ApplicationInfo> list;
    private long p;
    private SparseLongArray array;
    private List<applicationUIDModel> l3;

    public allAppUsageAsync(Context context, ProgressBar progressBar, RecyclerView recyclerView, int i2, int i3, RelativeLayout relativeLayout) {
        this.contextWeakReference = new WeakReference<>(context);
        this.progressBarWeakReference = new WeakReference<>(progressBar);
        this.relativeLayoutWeakReference = new WeakReference<>(relativeLayout);
        this.recyclerViewWeakReference = new WeakReference<>(recyclerView);
        this.networkType = i2;
        this.durationType = i3;
    }

    private void getData(int i2, int i3) {
        dailyUsageModel dailyUsageModel1;
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) this.contextWeakReference.get().getSystemService(Context.NETWORK_STATS_SERVICE);
        if (networkStatsManager != null) {
            switch (i2) {
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
                default:
                    dailyUsageModel1 = makeCalenderInstances.lastHour(this.contextWeakReference.get());
                    break;
            }
            if (i3 == 0) {
                try {
                    networkStats = networkStatsManager.querySummary(0, this.id1, dailyUsageModel1.getStart().getTimeInMillis(), dailyUsageModel1.getEnd().getTimeInMillis());
                } catch (RemoteException e2) {
                    e2.printStackTrace();
                }
            } else if (i3 == 1) {
                try {
                    networkStats = networkStatsManager.querySummary(0, this.id2, dailyUsageModel1.getStart().getTimeInMillis(), dailyUsageModel1.getEnd().getTimeInMillis());
                } catch (RemoteException e3) {
                    e3.printStackTrace();
                }
            } else if (i3 != 2) {
                try {
                    networkStats = networkStatsManager.querySummary(0, this.id1, dailyUsageModel1.getStart().getTimeInMillis(), dailyUsageModel1.getEnd().getTimeInMillis());
                } catch (RemoteException e4) {
                    e4.printStackTrace();
                }
            } else {
                try {
                    networkStats = networkStatsManager.querySummary(1, null, dailyUsageModel1.getStart().getTimeInMillis(), dailyUsageModel1.getEnd().getTimeInMillis());
                } catch (RemoteException e5) {
                    e5.printStackTrace();
                }
            }
        }
        this.array = new SparseLongArray();
        if (networkStats != null) {
            NetworkStats.Bucket bucket = new NetworkStats.Bucket();
            while (networkStats.hasNextBucket()) {
                networkStats.getNextBucket(bucket);
                if (this.array.indexOfKey(bucket.getUid()) < 0) {
                    this.array.put(bucket.getUid(), bucket.getRxBytes() + bucket.getTxBytes());
                } else {
                    this.array.put(bucket.getUid(), this.array.get(bucket.getUid()) + bucket.getRxBytes() + bucket.getTxBytes());
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
        this.l3 = new ArrayList<>();
        if (arrayList.size() > 0) {
            for (ApplicationInfo applicationInfo2 : arrayList) {
                if (this.array.indexOfKey(applicationInfo2.uid) > -1) {
                    applicationUIDModel applicationUIDModel = new applicationUIDModel();
                    applicationUIDModel.setLabel(applicationInfo2.loadLabel(packageManager).toString());
                    applicationUIDModel.setUid(applicationInfo2.uid);
                    applicationUIDModel.setPackageName(applicationInfo2.packageName);
                    applicationUIDModel.setUri(Uri.parse("android.resource://" + applicationInfo2.packageName + "/" + applicationInfo2.icon));
                    applicationUIDModel.setData(this.array.get(applicationInfo2.uid));
                    applicationUIDModel.setFormattedData(resultFormatter.formatVal((double) this.array.get(applicationInfo2.uid)));
                    this.p = this.p + this.array.get(applicationInfo2.uid);
                    this.l3.add(applicationUIDModel);
                }
            }
        }
        List<applicationUIDModel> list = this.l3;
        if (list != null && list.size() > 0) {
            Collections.sort(this.l3, new Comparator<applicationUIDModel>() {
                public int a(applicationUIDModel applicationUIDModel, applicationUIDModel applicationUIDModel2) {
                    if (applicationUIDModel2.getData() > applicationUIDModel.getData()) {
                        return 1;
                    }
                    return applicationUIDModel2.getData() == applicationUIDModel.getData() ? 0 : -1;
                }

                public int compare(applicationUIDModel applicationUIDModel, applicationUIDModel applicationUIDModel2) {
                    return a(applicationUIDModel, applicationUIDModel2);
                }
            });
            this.l1 = new ArrayList<>();
            long a2 = this.l3.get(0).getData();
            for (applicationUIDModel applicationUIDModel2 : this.l3) {
                int a3 = this.p != 0 ? (int) ((applicationUIDModel2.getData() * 100) / this.p) : 0;
                if (applicationUIDModel2.getData() == 0) {
                    applicationUIDModel2.setPercentage("0 %");
                } else if (a3 < 1) {
                    applicationUIDModel2.setPercent(1);
                    applicationUIDModel2.setPercentage("Less than 1 %");
                } else {
                    if (a2 != 0) {
                        applicationUIDModel2.setPercent((int) ((applicationUIDModel2.getData() * 100) / a2));
                    }
                    applicationUIDModel2.setPercentage(a3 + " %");
                }
                this.l1.add(applicationUIDModel2);
            }
        }
    }

    public List<applicationUIDModel> doInBackground(Integer... numArr) {
        try {
            List<ApplicationInfo> installedApplications = this.contextWeakReference.get().getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
            this.list = new ArrayList<>();
            for (ApplicationInfo applicationInfo : installedApplications) {
                if ((applicationInfo.flags & 1) == 0 || (applicationInfo.flags & 128) != 0) {
                    this.list.add(applicationInfo);
                }
            }
            this.l2 = new ArrayList<>();
            getData(this.networkType, this.durationType);
            return this.l1;
        } catch (Exception unused) {
            return this.l1;
        }
    }

    public void onPostExecute(List<applicationUIDModel> list) {
        try {
            if (this.contextWeakReference.get().getApplicationContext() != null) {
                this.progressBarWeakReference.get().setVisibility(View.INVISIBLE);
                if (list != null && list.size() > 0) {
                    this.relativeLayoutWeakReference.get().setVisibility(View.INVISIBLE);
                    this.recyclerViewWeakReference.get().setVisibility(View.VISIBLE);
                    this.recyclerViewWeakReference.get().setAdapter(new allAppUsageAdapter(this.contextWeakReference.get(), list));
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.contextWeakReference.get());
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    this.recyclerViewWeakReference.get().setLayoutManager(linearLayoutManager);
                    this.recyclerViewWeakReference.get().setItemAnimator(new DefaultItemAnimator());
                    this.progressBarWeakReference.get().setVisibility(View.INVISIBLE);
                } else {
                    this.relativeLayoutWeakReference.get().setVisibility(View.VISIBLE);
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
            if (this.id1 == null) {
                this.id1 = this.id2;
            }
            this.recyclerViewWeakReference.get().setVisibility(View.INVISIBLE);
            this.relativeLayoutWeakReference.get().setVisibility(View.VISIBLE);
        } catch (Exception unused) {
            super.onPreExecute();
        }
    }
}
