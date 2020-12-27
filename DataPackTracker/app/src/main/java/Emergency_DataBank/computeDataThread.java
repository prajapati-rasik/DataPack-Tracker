package Emergency_DataBank;

import android.annotation.SuppressLint;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.RemoteException;
import android.telephony.TelephonyManager;

public class computeDataThread implements Runnable{

    private final NetworkStatsManager networkStatsManager;
    private static long usedData = 0;
    private static boolean stopThread = true;
    SharedPreferences.Editor editor;
    final String DATA_LIMIT = "Used_Data";
    Context c;

    public computeDataThread(Context context, SharedPreferences.Editor e){
        networkStatsManager = (NetworkStatsManager)context.getSystemService(Context.NETWORK_STATS_SERVICE);
        c = context;
        editor = e;
    }

    public long getAllRxBytesMobile(Context context) {
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(context),
                    0,
                    System.currentTimeMillis());
        } catch (RemoteException e) {
            return -1;
        }
        return bucket.getRxBytes();
    }

    public long getAllTxBytesMobile(Context context) {
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(context),
                    0,
                    System.currentTimeMillis());
        } catch (RemoteException e) {
            return -1;
        }
        return bucket.getTxBytes();
    }

    @SuppressLint({"MissingPermission", "HardwareIds"})
    private String getSubscriberId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        assert tm != null;
        return tm.getSubscriberId();
    }

    public long getAllRxBytesWifi() {
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI,
                    "",
                    0,
                    System.currentTimeMillis());
        } catch (RemoteException e) {
            return -1;
        }
        return bucket.getRxBytes();
    }

    public long getAllTxBytesWifi() {
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI,
                    "",
                    0,
                    System.currentTimeMillis());
        } catch (RemoteException e) {
            return -1;
        }
        return bucket.getTxBytes();
    }

    public long getdata(){
        return (getAllTxBytesWifi()/1000)+(getAllRxBytesWifi()/1000);
    }

    public static void setStopThread(boolean x){
        stopThread = x;
    }

    public static boolean getStopThread(){
        return stopThread;
    }

    public static long getUsedData(){
        return usedData;
    }

    @Override
    public void run() {
        while(!stopThread){
            try {
                Thread.sleep(1000);
                usedData = getdata();
                editor.putString(DATA_LIMIT, String.valueOf(usedData));
                editor.commit();
            }catch (Exception e){
                usedData = getdata();
                editor.putLong(DATA_LIMIT, usedData);
                editor.commit();
            }
        }
    }
}
