package Usage_calculation;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Process;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionChecker {

    static final boolean desiredAssertionStatus = (!PermissionChecker.class.desiredAssertionStatus());

    public static boolean checkPhoneStatePermission(Context context, Activity activity){
        if(!checkPhoneStatePermissionHelper(context)){
            requestPhoneStatePermission(context, activity);
        }
        return checkPhoneStatePermissionHelper(context);
    }

    private static void requestPhoneStatePermission(Context context, Activity activity) {
        SharedPreferences sharedPrefer = context.getSharedPreferences("AllPermissions", Context.MODE_PRIVATE);
        if(checkPhoneStatePermissionHelper(context)) return;
        if(ActivityCompat.shouldShowRequestPermissionRationale(activity, "android.permission.READ_PHONE_STATE")){
            ActivityCompat.requestPermissions(activity, new String[]{"android.permission.READ_PHONE_STATE"}, 37);
        }else if(sharedPrefer.getBoolean("android.permission.READ_PHONE_STATE", false)){
            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package",context.getPackageName(),null));
            context.startActivity(intent);
        }else{
            ActivityCompat.requestPermissions(activity, new String[]{"android.permission.READ_PHONE_STATE"}, 37);
            SharedPreferences.Editor editor = sharedPrefer.edit();
            editor.putBoolean("android.permission.READ_PHONE_STATE", true);
            editor.apply();
        }
    }

    public static boolean checkUsageAccessPermission(Context context){
        AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        if (!desiredAssertionStatus && appOpsManager == null) {
            throw new AssertionError();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            assert appOpsManager != null;
            return appOpsManager.unsafeCheckOpNoThrow("android:get_usage_stats", Process.myUid(), context.getPackageName()) == AppOpsManager.MODE_ALLOWED;
        } else {
            assert appOpsManager != null;
            return appOpsManager.checkOpNoThrow("android:get_usage_stats", Process.myUid(), context.getPackageName()) == AppOpsManager.MODE_ALLOWED;
        }
    }

    public static void checkUsageAccessPermissionWithWatchingMode(final Context context, final Activity activity){
        final AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        if (!desiredAssertionStatus && appOpsManager == null) {
            throw new AssertionError();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            assert appOpsManager != null;
            appOpsManager.unsafeCheckOpNoThrow("android:get_usage_stats", Process.myUid(), context.getPackageName());
            return;
        } else {
            assert appOpsManager != null;
            appOpsManager.startWatchingMode("android:get_usage_stats", context.getPackageName(), new AppOpsManager.OnOpChangedListener() {
                @Override
                public void onOpChanged(String op, String packageName) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        if (appOpsManager.unsafeCheckOpNoThrow("android:get_usage_stats", Process.myUid(), context.getPackageName()) == AppOpsManager.MODE_ALLOWED) {
                            SubscriberDetails.setAllSubscriptionDetails(context, activity);
                            appOpsManager.stopWatchingMode(this);
                        }
                    }else{
                        if (appOpsManager.checkOpNoThrow("android:get_usage_stats", Process.myUid(), context.getPackageName()) == AppOpsManager.MODE_ALLOWED) {
                            SubscriberDetails.setAllSubscriptionDetails(context, activity);
                            appOpsManager.stopWatchingMode(this);
                        }
                    }
                }
            });
        }
        requestUsageAccessPermission(context);
        if(checkUsageAccessPermission(context)){
            SharedPreferences sharedPrefer = context.getSharedPreferences("AllPermissions", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPrefer.edit();
            editor.putBoolean("android.", true);
            editor.apply();
        }
    }

    private static boolean checkPhoneStatePermissionHelper(Context context){
        return ContextCompat.checkSelfPermission(context, "android.permission.READ_PHONE_STATE") == PackageManager.PERMISSION_GRANTED;
    }

    private static void requestUsageAccessPermission(Context context) {
        context.startActivity(new Intent("android.settings.USAGE_ACCESS_SETTINGS"));
    }
}
