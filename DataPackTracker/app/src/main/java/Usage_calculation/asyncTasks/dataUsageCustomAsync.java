package Usage_calculation.asyncTasks;

import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.datapacktracker.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import Usage_calculation.getNetStats;
import Usage_calculation.models.ForegroundBackgroundModel;
import Usage_calculation.models.dailyUsageModel;
import Usage_calculation.models.dataUsageAllStringModel;
import Usage_calculation.models.dataUsageIntervalTypeModel;
import Usage_calculation.models.singleSimModel;
import Usage_calculation.resultFormatter;
import dataUsage.Fragments.DataUsageCustomFragment;

public class dataUsageCustomAsync extends AsyncTask<Integer, Integer, List<dataUsageAllStringModel>> {
    private final WeakReference<Context> contextWeakReference;
    private final WeakReference<View> viewWeakReference;
    private final WeakReference<ProgressBar> progressBarWeakReference;
    private List<dailyUsageModel> list;
    private int uid;
    private SharedPreferences sharedPreferences;
    private  ArrayList<dataUsageIntervalTypeModel> llist;
    private boolean is2SIM;

    public dataUsageCustomAsync(View view, Context context, ProgressBar progressBar, List<dailyUsageModel> list, int i2) {
        this.contextWeakReference = new WeakReference<>(context);
        this.viewWeakReference = new WeakReference<>(view);
        this.progressBarWeakReference = new WeakReference<>(progressBar);
        this.list = list;
        this.uid = i2;
    }

    public List<dataUsageAllStringModel> doInBackground(Integer... numArr) {
        int i2 = 0;
        boolean isDualSim = this.sharedPreferences.getBoolean("isDualSim", false);
        is2SIM = isDualSim;
        String id1 = this.sharedPreferences.getString("subscriberId1", null);
        String id2 = this.sharedPreferences.getString("subscriberId2", null);
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) this.contextWeakReference.get().getSystemService(Context.NETWORK_STATS_SERVICE);
        ArrayList<dataUsageIntervalTypeModel> arrayList = new ArrayList<>();
        ArrayList<dataUsageAllStringModel> resultList = new ArrayList<>();
        int size = this.list.size();
        try {
            dataUsageIntervalTypeModel intervalTypeModel;
            singleSimModel model;
            if (this.uid != 0) {
                dailyUsageModel dailyUsageModel = new dailyUsageModel();
                dailyUsageModel.setStart(this.list.get(0).getStart());
                dailyUsageModel.setEnd(this.list.get(0).getEnd());
                dailyUsageModel.setType(this.list.get(0).getType());
                if (isDualSim) {
                    intervalTypeModel = new dataUsageIntervalTypeModel();
                    ForegroundBackgroundModel fbmodel = getNetStats.getDataFBUID(1, dailyUsageModel, this.uid, networkStatsManager, null);
                    intervalTypeModel.setReceivedWifi(fbmodel.getForegroundRx() + fbmodel.getBackgroundRx());
                    intervalTypeModel.setSentWifi(fbmodel.getForegroundTx() + fbmodel.getBackgroundTx());
                    fbmodel = getNetStats.getDataFBUID(0, dailyUsageModel, this.uid, networkStatsManager, id1);
                    intervalTypeModel.setReceivedSIM1(fbmodel.getForegroundRx() + fbmodel.getBackgroundRx());
                    intervalTypeModel.setSentSIM1(fbmodel.getForegroundTx() + fbmodel.getBackgroundTx());
                    fbmodel = getNetStats.getDataFBUID(0, dailyUsageModel, this.uid, networkStatsManager, id2);
                    intervalTypeModel.setReceivedSIM1(fbmodel.getForegroundRx() + fbmodel.getBackgroundRx());
                    intervalTypeModel.setSentSIM1(fbmodel.getForegroundTx() + fbmodel.getBackgroundTx());
                    intervalTypeModel.setType(dailyUsageModel.getType());
                    arrayList.add(intervalTypeModel);
                } else {
                    intervalTypeModel = new dataUsageIntervalTypeModel();
                    ForegroundBackgroundModel fbmodel = getNetStats.getDataFBUID(1, dailyUsageModel, this.uid, networkStatsManager, null);
                    intervalTypeModel.setReceivedWifi(fbmodel.getForegroundRx() + fbmodel.getBackgroundRx());
                    intervalTypeModel.setSentWifi(fbmodel.getForegroundTx() + fbmodel.getBackgroundTx());
                    fbmodel = getNetStats.getDataFBUID(0, dailyUsageModel, this.uid, networkStatsManager, id1);
                    intervalTypeModel.setReceivedSIM1(fbmodel.getForegroundRx() + fbmodel.getBackgroundRx());
                    intervalTypeModel.setSentSIM1(fbmodel.getForegroundTx() + fbmodel.getBackgroundTx());
                    intervalTypeModel.setType(dailyUsageModel.getType());
                    arrayList.add(intervalTypeModel);
                }
            } else if (isDualSim) {
                while (i2 < size) {
                    intervalTypeModel = new dataUsageIntervalTypeModel();
                    model = getNetStats.getDataWifi(this.list.get(i2).getStart(), this.list.get(i2).getEnd(), networkStatsManager);
                    intervalTypeModel.setReceivedWifi(model.getRx());
                    intervalTypeModel.setSentWifi(model.getTx());
                    model = getNetStats.getDataMobile(this.list.get(i2).getStart(), this.list.get(i2).getEnd(), id1, networkStatsManager);
                    intervalTypeModel.setReceivedSIM1(model.getRx());
                    intervalTypeModel.setSentSIM1(model.getTx());
                    model = getNetStats.getDataMobile(this.list.get(i2).getStart(), this.list.get(i2).getEnd(), id2, networkStatsManager);
                    intervalTypeModel.setReceivedSIM2(model.getRx());
                    intervalTypeModel.setSentSIM2(model.getTx());
                    intervalTypeModel.setType(this.list.get(i2).getType());
                    arrayList.add(intervalTypeModel);
                    i2++;
                }
            } else {
                while (i2 < size) {
                    intervalTypeModel = new dataUsageIntervalTypeModel();
                    model = getNetStats.getDataWifi(this.list.get(i2).getStart(), this.list.get(i2).getEnd(), networkStatsManager);
                    intervalTypeModel.setReceivedWifi(model.getRx());
                    intervalTypeModel.setSentWifi(model.getTx());
                    model = getNetStats.getDataMobile(this.list.get(i2).getStart(), this.list.get(i2).getEnd(), id1, networkStatsManager);
                    intervalTypeModel.setReceivedSIM1(model.getRx());
                    intervalTypeModel.setSentSIM1(model.getTx());
                    intervalTypeModel.setType(this.list.get(i2).getType());
                    arrayList.add(intervalTypeModel);
                    i2++;
                }
            }
            this.llist = arrayList;
            for (dataUsageIntervalTypeModel m : arrayList) {
                dataUsageAllStringModel strings = new dataUsageAllStringModel();
                strings.setTitle(m.getType());
                strings.setWifiRx(resultFormatter.formatVal((double) m.getReceivedWifi()));
                strings.setWifiTx(resultFormatter.formatVal((double) m.getSentWifi()));
                strings.setWifiTotal(resultFormatter.formatVal((double) (m.getReceivedWifi() + m.getSentWifi())));
                strings.setSim1Rx(resultFormatter.formatVal((double) m.getReceivedSIM1()));
                strings.setSim1Tx(resultFormatter.formatVal((double) m.getSentSIM1()));
                strings.setSim1Total(resultFormatter.formatVal((double) (m.getReceivedSIM1() + m.getSentSIM1())));
                if (isDualSim) {
                    strings.setSim2Rx(resultFormatter.formatVal((double) m.getReceivedSIM2()));
                    strings.setSim2Tx(resultFormatter.formatVal((double) m.getSentSIM2()));
                    strings.setSim2Total(resultFormatter.formatVal((double) (m.getReceivedSIM2() + m.getSentSIM2())));
                }
                resultList.add(strings);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return resultList;
    }

    public void onPostExecute(List<dataUsageAllStringModel> list) {
        if (this.viewWeakReference.get() != null) {
            ((RelativeLayout) this.viewWeakReference.get().findViewById(R.id.containerRelativeLayoutCustom)).setVisibility(View.VISIBLE);
            DataUsageCustomFragment.updateRecycleView(this.viewWeakReference.get(), list);
            DataUsageCustomFragment.updateChart(this.viewWeakReference.get(), llist, is2SIM);
            this.progressBarWeakReference.get().setVisibility(View.INVISIBLE);
        }
        super.onPostExecute(list);
    }

    public void onPreExecute() {
        this.sharedPreferences = this.contextWeakReference.get().getSharedPreferences("MultipleSim", Context.MODE_PRIVATE);
        this.progressBarWeakReference.get().setVisibility(View.VISIBLE);
        super.onPreExecute();
    }
}