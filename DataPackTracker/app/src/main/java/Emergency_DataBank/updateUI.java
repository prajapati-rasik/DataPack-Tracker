package Emergency_DataBank;

import android.content.SharedPreferences;
import android.widget.ProgressBar;

public class updateUI implements Runnable {

    final String MyPREFERENCES = "Emergency_DataBank" ;
    final String DATA_LIMIT = "Data_Limit";
    final String TIME_LIMIT = "Time_Limit";
    final String DATA_USAGE = "Used_Data";

    ProgressBar timeProgressBar;
    ProgressBar dataProgressBar;
    SharedPreferences sharedpreferences;
    public static boolean running = true;

    public updateUI(ProgressBar x,ProgressBar y,SharedPreferences s){
        timeProgressBar = x;
        dataProgressBar = y;
        sharedpreferences = s;
    }

    public static void setRunning(boolean x){
        running = x;
    }

    @Override
    public void run() {
        while(running){
            try {
                Thread.sleep(1000);
                long usedData = sharedpreferences.getLong(DATA_USAGE,0);
                timeProgressBar.setProgress(10);
                dataProgressBar.setProgress(5);
            }
            catch (Exception e){
                long usedData = sharedpreferences.getLong(DATA_USAGE,0);
                timeProgressBar.setProgress(10);
                dataProgressBar.setProgress(5);
            }
        }
    }
}
