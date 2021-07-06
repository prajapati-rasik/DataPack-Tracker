package speedTesting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.datapacktracker.R;
import com.jignesh13.speedometer.SpeedoMeterView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.Objects;


public class speedTesting extends AppCompatActivity {

    private TextView total;
    private SpeedoMeterView speedoMeterView;
    private ProgressBar progressBar;

    private static final String TAG = speedTesting.class.getSimpleName();
    private static final int EXPECTED_SIZE_IN_BYTES = 1048576;

    private static final double EDGE_THRESHOLD = 176.0;
    private static final double BYTE_TO_KILOBIT = 0.0078125;
    private static final double KILOBIT_TO_MEGABIT = 0.0009765625;

    private final int MSG_UPDATE_STATUS=0;
    private final int MSG_UPDATE_CONNECTION_TIME=1;
    private final int MSG_COMPLETE_STATUS=2;

    private final static int UPDATE_THRESHOLD=300;
    private DecimalFormat mDecimalFormater;

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(final Message msg) {
            switch(msg.what){
                case MSG_UPDATE_STATUS:
                    final speedTesting.SpeedInfo info1=(speedTesting.SpeedInfo) msg.obj;
                    String totalString = mDecimalFormater.format(info1.kilobits);
                    total.setText(totalString);
                    speedoMeterView.setSpeed((int) info1.kilobits, true);
                    progressBar.setProgress(msg.arg1);
                    break;
                case MSG_UPDATE_CONNECTION_TIME:
                    totalString = "0";
                    total.setText(totalString);
                    speedoMeterView.setSpeed((int) msg.arg1, true);
                    break;
                case MSG_COMPLETE_STATUS:
                    final speedTesting.SpeedInfo info2=(speedTesting.SpeedInfo) msg.obj;
                    totalString = mDecimalFormater.format(info2.kilobits);;
                    total.setText(totalString);
                    speedoMeterView.setSpeed((int) info2.kilobits, true);
                    progressBar.setProgress(100);
                    progressBar.setVisibility(View.GONE);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    private final Runnable mWorker= () -> {
        InputStream stream = null;
        try {
            int bytesIn=0;
            String downloadFileUrl = "https://drive.google.com/file/d/1cqLVp5HyaflubxP4LhCO3h56xH7jJW4A/view?usp=sharing";
            long startCon = System.currentTimeMillis();
            URL url = new URL(downloadFileUrl);
            URLConnection con=url.openConnection();
            con.setUseCaches(false);
            long connectionLatency = System.currentTimeMillis() - startCon;
            stream=con.getInputStream();

            Message msgUpdateConnection=Message.obtain(mHandler, MSG_UPDATE_CONNECTION_TIME);
            msgUpdateConnection.arg1 = (int) connectionLatency;
            mHandler.sendMessage(msgUpdateConnection);

            long start=System.currentTimeMillis();
            int currentByte=0;
            long updateStart=System.currentTimeMillis();
            long updateDelta=0;
            int  bytesInThreshold=0;

            while((currentByte=stream.read())!=-1){
                bytesIn++;
                bytesInThreshold++;
                if(updateDelta>=UPDATE_THRESHOLD){
                    int progress=(int)((bytesIn/(double)EXPECTED_SIZE_IN_BYTES)*100);
                    Message msg=Message.obtain(mHandler, MSG_UPDATE_STATUS, calculate(updateDelta, bytesInThreshold));
                    msg.arg1=progress;
                    msg.arg2=bytesIn;
                    mHandler.sendMessage(msg);
                    //Reset
                    updateStart=System.currentTimeMillis();
                    bytesInThreshold=0;
                }
                updateDelta = System.currentTimeMillis()- updateStart;
            }

            long downloadTime=(System.currentTimeMillis()-start);
            if(downloadTime==0){
                downloadTime=1;
            }

            Message msg=Message.obtain(mHandler, MSG_COMPLETE_STATUS, calculate(downloadTime, bytesIn));
            msg.arg1=bytesIn;
            mHandler.sendMessage(msg);
        } catch (IOException e) {
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
        } finally{
            try {
                if(stream!=null){
                    stream.close();
                }
            } catch (IOException e) {
                Log.e(TAG, Objects.requireNonNull(e.getMessage()));
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speedtesting);

        mDecimalFormater=new DecimalFormat("##.##");
        total = findViewById(R.id.totalSpeed);
        speedoMeterView = findViewById(R.id.speedometerview);
        progressBar = findViewById(R.id.speedTestingProgressBar);

        progressBar.setVisibility(View.VISIBLE);

        if(isConnected()) {
            new Thread(mWorker).start();
        }else{
            total.setText("0");
        }
    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    private SpeedInfo calculate(final long downloadTime, final long bytesIn){
        SpeedInfo info=new SpeedInfo();
        long bytespersecond = (bytesIn / downloadTime) * 1000;
        double kilobits = (bytespersecond * BYTE_TO_KILOBIT)/8;
        double megabits = kilobits  * KILOBIT_TO_MEGABIT;
        info.downspeed=bytespersecond;
        info.kilobits=kilobits;
        info.megabits=megabits;
        return info;
    }

    private static class SpeedInfo{
        public double kilobits=0;
        public double megabits=0;
        public double downspeed=0;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
