package Usage_calculation;

import android.content.Context;
import android.net.ConnectivityManager;

public class connectivityChecker {

    public static boolean checkConnectivity(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (!(connectivityManager == null || connectivityManager.getActiveNetworkInfo() == null)) {
                if (connectivityManager.getActiveNetworkInfo().isConnected()) {
                    return true;
                }
            }
            return false;
        } catch (Exception unused) {
            return false;
        }
    }
}
