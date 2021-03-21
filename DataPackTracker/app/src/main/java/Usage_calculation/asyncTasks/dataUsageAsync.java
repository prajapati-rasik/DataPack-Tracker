package Usage_calculation.asyncTasks;

import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datapacktracker.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import Usage_calculation.adapters.DataUsageCalculationFragmentAdapter;
import Usage_calculation.getNetStats;
import Usage_calculation.models.dailyUsageModel;
import Usage_calculation.models.dataUsageAllStringModel;
import Usage_calculation.models.dataUsageIntervalTypeModel;
import Usage_calculation.models.singleSimModel;
import Usage_calculation.resultFormatter;

public class dataUsageAsync extends AsyncTask<Integer, Integer, List<dataUsageAllStringModel>> {

    private final WeakReference<Context> context;
    private final WeakReference<View> view;
    private final WeakReference<ProgressBar> progressbar;
    private List<dailyUsageModel> resultModels;
    private SharedPreferences sharedPreferences;
    private dataUsageIntervalTypeModel intervalTypeModel;

    public dataUsageAsync(Context context, View view, ProgressBar progressBar, List<dailyUsageModel> list){
        this.context = new WeakReference<>(context);
        this.view = new WeakReference<>(view);
        this.progressbar = new WeakReference<>(progressBar);
        this.resultModels = list;
    }

    @Override
    protected List<dataUsageAllStringModel> doInBackground(Integer... integers) {
        int i = 0;
        boolean valueOf = this.sharedPreferences.getBoolean("isDualSim", false);
        String id1 = this.sharedPreferences.getString("subscriberId1", null);
        String id2 = this.sharedPreferences.getString("subscriberId2", null);
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) this.context.get().getSystemService(Context.NETWORK_STATS_SERVICE);
        ArrayList<dataUsageAllStringModel> arrayList = new ArrayList<>();
        int size = this.resultModels.size();
        ArrayList<dataUsageIntervalTypeModel> resultList = new ArrayList<>();
        try {
            if (valueOf) {
                while (i < size && !isCancelled()) {
                    publishProgress((i * 100) / size);
                    this.intervalTypeModel = new dataUsageIntervalTypeModel();
                    singleSimModel wifi = getNetStats.getDataWifi(this.resultModels.get(i).getStart(), this.resultModels.get(i).getEnd(), networkStatsManager);
                    this.intervalTypeModel.setReceivedWifi(wifi.getRx());
                    this.intervalTypeModel.setSentWifi(wifi.getTx());
                    singleSimModel sim1Data = getNetStats.getDataMobile(this.resultModels.get(i).getStart(), this.resultModels.get(i).getEnd(), id1, networkStatsManager);
                    this.intervalTypeModel.setReceivedSIM1(sim1Data.getRx());
                    this.intervalTypeModel.setSentSIM1(sim1Data.getTx());
                    singleSimModel sim2Data = getNetStats.getDataMobile(this.resultModels.get(i).getStart(), this.resultModels.get(i).getEnd(), id2, networkStatsManager);
                    this.intervalTypeModel.setReceivedSIM2(sim2Data.getRx());
                    this.intervalTypeModel.setSentSIM2(sim2Data.getTx());
                    this.intervalTypeModel.setType(this.resultModels.get(i).getType());
                    resultList.add(this.intervalTypeModel);
                    i++;
                }
            } else {
                while (i < size && !isCancelled()) {
                    publishProgress((i * 100) / size);
                    this.intervalTypeModel = new dataUsageIntervalTypeModel();
                    singleSimModel wifi = getNetStats.getDataWifi(this.resultModels.get(i).getStart(), this.resultModels.get(i).getEnd(), networkStatsManager);
                    this.intervalTypeModel.setReceivedWifi(wifi.getRx());
                    this.intervalTypeModel.setSentWifi(wifi.getTx());
                    singleSimModel sim1Data = getNetStats.getDataMobile(this.resultModels.get(i).getStart(), this.resultModels.get(i).getEnd(), id1, networkStatsManager);
                    this.intervalTypeModel.setReceivedSIM1(sim1Data.getRx());
                    this.intervalTypeModel.setSentSIM1(sim1Data.getTx());
                    this.intervalTypeModel.setType(this.resultModels.get(i).getType());
                    resultList.add(this.intervalTypeModel);
                    i++;
                }
            }
            for (dataUsageIntervalTypeModel model : resultList) {
                dataUsageAllStringModel strings = new dataUsageAllStringModel();
                strings.setTitle(model.getType());
                strings.setWifiRx(resultFormatter.formatVal((double) model.getReceivedWifi()));
                strings.setWifiTx(resultFormatter.formatVal((double) model.getSentWifi()));
                strings.setWifiTotal(resultFormatter.formatVal((double) (model.getReceivedWifi() + model.getSentWifi())));
                strings.setSim1Rx(resultFormatter.formatVal((double) model.getReceivedSIM1()));
                strings.setSim1Tx(resultFormatter.formatVal((double) model.getSentSIM1()));
                strings.setSim1Total(resultFormatter.formatVal((double) (model.getReceivedSIM1() + model.getSentSIM1())));
                if (valueOf) {
                    strings.setSim2Rx(resultFormatter.formatVal((double) model.getReceivedSIM2()));
                    strings.setSim2Tx(resultFormatter.formatVal((double) model.getSentSIM2()));
                    strings.setSim2Total(resultFormatter.formatVal((double) (model.getReceivedSIM2() + model.getSentSIM2())));
                }
                arrayList.add(strings);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return arrayList;
    }

    public void onPostExecute(List<dataUsageAllStringModel> list) {
        if (!(this.view.get() == null || this.progressbar.get() == null)) {
            this.view.get().setVisibility(View.VISIBLE);
            this.progressbar.get().setVisibility(View.GONE);
            RecyclerView recyclerView = (RecyclerView) view.get().findViewById(R.id.data_usage_recyclerView_daily);
            recyclerView.setAdapter(new DataUsageCalculationFragmentAdapter(view.get().getContext(), list));
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.get().getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            this.intervalTypeModel = null;
            this.resultModels = null;
        }
        super.onPostExecute(null);
    }

    public void onProgressUpdate(Integer... numArr) {
        if (this.progressbar.get() != null) {
            this.progressbar.get().setProgress(numArr[0]);
        }
        super.onProgressUpdate(numArr[0]);
    }

    public void onPreExecute() {
        this.sharedPreferences = this.context.get().getSharedPreferences("MultipleSim", Context.MODE_PRIVATE);
        this.progressbar.get().setVisibility(View.VISIBLE);
        super.onPreExecute();
    }
}
