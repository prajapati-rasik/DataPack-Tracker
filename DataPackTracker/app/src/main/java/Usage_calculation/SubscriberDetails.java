package Usage_calculation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class SubscriberDetails {

    private static String subscriberId1;
    private static String subscriberId2;
    private static String simName1;
    private static String simName2;
    private static boolean isSimReady1;
    private static boolean isSimReady2;
    private static boolean isDualSim;
    private static String simIMEI1;
    private static String simIMEI2;
    private static String simCarrier1;
    private static String simCarrier2;
    private static SharedPreferences.Editor editor;

    @SuppressLint("HardwareIds")
    public static void setAllSubscriptionDetails(Context context, Activity activity) {
        TelephonyInfo telephonyInfo;
        Method method = null;

        if(PermissionChecker.checkPhoneStatePermission(context, activity)){
            editor = context.getSharedPreferences("MultipleSim", Context.MODE_PRIVATE).edit();
            telephonyInfo = TelephonyInfo.getInstance(context, activity);
            if(telephonyInfo != null){
                isSimReady1 = telephonyInfo.isSIM1Ready();
                isSimReady2 = telephonyInfo.isSIM2Ready();
                isDualSim = telephonyInfo.isDualSIM();
                simIMEI1 = telephonyInfo.getImsiSIM1();
                simIMEI2 = telephonyInfo.getImsiSIM2();
                editor.putBoolean("isSimReady1", isSimReady1);
                editor.putBoolean("isSimReady2", isSimReady2);
                editor.putBoolean("isDualSim", isDualSim);
                editor.putString("simIMEI1", simIMEI1);
                editor.putString("simIMEI2", simIMEI2);
                List<SubscriptionInfo> activeSubscriptionInfoList = SubscriptionManager.from(context).getActiveSubscriptionInfoList();
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                if(!isDualSim){
                    if(isSimReady1 || isSimReady2){
                        assert telephonyManager != null;
                        subscriberId1 = telephonyManager.getSubscriberId();
                        if(activeSubscriptionInfoList != null && activeSubscriptionInfoList.size() > 0){
                            simName1 = (String) activeSubscriptionInfoList.get(0).getDisplayName();
                            simCarrier1 = (String) activeSubscriptionInfoList.get(0).getCarrierName();
                        }
                        editor.putString("subscriberId1",subscriberId1);
                        editor.putString("simName1",simName1);
                        editor.putString("simCarrier1",simCarrier1);
                    }
                }else if(activeSubscriptionInfoList == null || activeSubscriptionInfoList.size() <= 1){
                    assert telephonyManager != null;
                    subscriberId1 = telephonyManager.getSubscriberId();
                    editor.putBoolean("isDualSim", false);
                    if(activeSubscriptionInfoList != null || activeSubscriptionInfoList.size() > 0){
                        simName1 = (String)activeSubscriptionInfoList.get(0).getDisplayName();
                        simCarrier1 = (String) activeSubscriptionInfoList.get(0).getCarrierName();
                    }
                    editor.putString("subscriberId1",subscriberId1);
                    editor.putString("simName1",simName1);
                    editor.putString("simCarrier1",simCarrier1);
                }else{
                    try {
                        method = Class.forName(telephonyManager.getClass().getName()).getMethod("getSubscriberId", Integer.TYPE);
                    }catch (ClassNotFoundException | NoSuchMethodException e){
                        e.printStackTrace();
                    }
                    if(isSimReady1 && isSimReady2){
                        if(method != null){
                            try {
                                subscriberId1 = (String) method.invoke(telephonyManager, activeSubscriptionInfoList.get(0).getSubscriptionId());
                                subscriberId2 = (String) method.invoke(telephonyManager, activeSubscriptionInfoList.get(1).getSubscriptionId());
                                simName1 = (String) activeSubscriptionInfoList.get(0).getDisplayName();
                                simName2 = (String) activeSubscriptionInfoList.get(1).getDisplayName();
                                simCarrier1 = (String) activeSubscriptionInfoList.get(0).getCarrierName();
                                simCarrier2 = (String) activeSubscriptionInfoList.get(1).getCarrierName();
                                editor.putString("subscriberId1", subscriberId1);
                                editor.putString("subscriberId2", subscriberId2);
                                editor.putString("simName1", simName1);
                                editor.putString("simName2", simName2);
                                editor.putString("simCarrier1",simCarrier1);
                                editor.putString("simCarrier2",simCarrier2);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }else if(isSimReady1 && !isSimReady2){
                        if(method != null) {
                            try {
                                subscriberId1 = (String) method.invoke(telephonyManager, activeSubscriptionInfoList.get(0).getSubscriptionId());
                                simName1 = (String) activeSubscriptionInfoList.get(0).getDisplayName();
                                simCarrier1 = (String) activeSubscriptionInfoList.get(0).getCarrierName();
                                editor.putString("subscriberId1", subscriberId1);
                                editor.putString("simName1", simName1);
                                editor.putString("simCarrier1",simCarrier1);
                                editor.putBoolean("isDualSim", false);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }else if(!isSimReady1 && isSimReady2){
                        if(method != null) {
                            try {
                                subscriberId2 = (String) method.invoke(telephonyManager, activeSubscriptionInfoList.get(1).getSubscriptionId());
                                simName2 = (String) activeSubscriptionInfoList.get(1).getDisplayName();
                                simCarrier2 = (String) activeSubscriptionInfoList.get(1).getCarrierName();
                                editor.putString("subscriberId2", subscriberId2);
                                editor.putString("simName2", simName2);
                                editor.putString("simCarrier2",simCarrier2);
                                editor.putBoolean("isDualSim", false);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }else{
                        editor.putBoolean("isDualSim", false);
                    }
                }
                editor.apply();
            }else{
                editor.putString("simIMEI1", null);
                editor.putString("simIMEI2", null);
                editor.putString("simName1", null);
                editor.putString("simName2", null);
                editor.putString("subscriptionId1", null);
                editor.putString("subscriptionId2", null);
                editor.putBoolean("isSimReady1", false);
                editor.putBoolean("isSimReady2", false);
                editor.putBoolean("isDualSim", false);
                editor.putString("simCarrier1",null);
                editor.putString("simCarrier2",null);
                editor.apply();
            }
        }else{
            editor.putString("simIMEI1", null);
            editor.putString("simIMEI2", null);
            editor.putString("simName1", null);
            editor.putString("simName2", null);
            editor.putString("subscriptionId1", null);
            editor.putString("subscriptionId2", null);
            editor.putBoolean("isSimReady1", false);
            editor.putBoolean("isSimReady2", false);
            editor.putString("simCarrier1",null);
            editor.putString("simCarrier2",null);
            editor.putBoolean("isDualSim", false);
            editor.apply();
        }
    }

}
