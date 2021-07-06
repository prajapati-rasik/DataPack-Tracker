package Usage_calculation;

import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.net.ConnectivityManager;
import android.os.RemoteException;
import android.util.Log;
import android.util.SparseLongArray;

import java.util.Calendar;

import Usage_calculation.models.ForegroundBackgroundModel;
import Usage_calculation.models.dailyUsageModel;
import Usage_calculation.models.singleSimModel;

public class getNetStats {

    private static singleSimModel oneModel;
    private static NetworkStats.Bucket bucket;

    public static ForegroundBackgroundModel getDataFBUID(int network, dailyUsageModel dmodel, int uid, NetworkStatsManager networkStatsManager, String str) {
        ForegroundBackgroundModel twoModel = new ForegroundBackgroundModel();
        twoModel.setForegroundTx(0);
        twoModel.setForegroundRx(0);
        twoModel.setBackgroundTx(0);
        twoModel.setBackgroundRx(0);
        try {
            NetworkStats networkStats = networkStatsManager.querySummary(network, str, dmodel.getStart().getTimeInMillis(), dmodel.getEnd().getTimeInMillis());
            if (networkStats != null) {
                NetworkStats.Bucket bucket = new NetworkStats.Bucket();
                while (networkStats.hasNextBucket()) {
                    networkStats.getNextBucket(bucket);
                    if (uid == bucket.getUid()) {
                        if (bucket.getState() == NetworkStats.Bucket.STATE_FOREGROUND) {
                            twoModel.setForegroundRx(twoModel.getForegroundRx() + bucket.getRxBytes());
                            twoModel.setForegroundTx(twoModel.getForegroundTx() + bucket.getTxBytes());
                        } else {
                            twoModel.setBackgroundRx(twoModel.getBackgroundRx() + bucket.getRxBytes());
                            twoModel.setBackgroundTx(twoModel.getBackgroundTx() + bucket.getTxBytes());
                        }
                    }
                }
            }
        } catch (RemoteException e2) {
            e2.printStackTrace();
        }
        return twoModel;
    }

    public static singleSimModel getDataWifi(Calendar calendar1, Calendar calendar2, NetworkStatsManager networkStatsManager) {
        oneModel = new singleSimModel();
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI, null, calendar1.getTimeInMillis(), calendar2.getTimeInMillis());
        } catch (RemoteException | SecurityException ignored) {
        }
        NetworkStats.Bucket buck = bucket;
        if (buck != null) {
            oneModel.setRx(buck.getRxBytes());
            oneModel.setTx(buck.getTxBytes());
        }
        return oneModel;
    }

    public static singleSimModel getDataMobile(Calendar calendar1, Calendar calendar2, String str, NetworkStatsManager networkStatsManager) {
        oneModel = new singleSimModel();
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE, str, calendar1.getTimeInMillis(), calendar2.getTimeInMillis());
        } catch (RemoteException | SecurityException ignored) {
        }
        NetworkStats.Bucket buck = bucket;
        if (buck != null) {
            oneModel.setRx(buck.getRxBytes());
            oneModel.setTx(buck.getTxBytes());
        }
        return oneModel;
    }
}
