package Usage_calculation.asyncTasks;

import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datapacktracker.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import Usage_calculation.XYmarkerView;
import Usage_calculation.adapters.DataUsageCalculationFragmentAdapter;
import Usage_calculation.getNetStats;
import Usage_calculation.models.ForegroundBackgroundModel;
import Usage_calculation.models.dailyUsageModel;
import Usage_calculation.models.dataUsageAllStringModel;
import Usage_calculation.models.dataUsageIntervalTypeModel;
import Usage_calculation.models.singleSimModel;
import Usage_calculation.resultFormatter;
import dataUsage.Fragments.DataUsageCustomFragment;

public class appUsageAsync extends AsyncTask<Integer, Integer, List<dataUsageAllStringModel>> {
    private final WeakReference<Context> contextWeakReference;
    private final WeakReference<View> viewWeakReference;
    private final WeakReference<ProgressBar> progressBarWeakReference;
    private List<dailyUsageModel> list;
    private int uid;
    private SharedPreferences sharedPreferences;
    private ArrayList<dataUsageIntervalTypeModel> llist;
    private boolean is2SIM;
    private WeakReference<PieChart> piechart;
    private WeakReference<BarChart> barchart;

    public appUsageAsync(View view, Context context, ProgressBar progressBar, List<dailyUsageModel> list, int i2, PieChart pie, BarChart bar) {
        this.contextWeakReference = new WeakReference<>(context);
        this.viewWeakReference = new WeakReference<>(view);
        this.progressBarWeakReference = new WeakReference<>(progressBar);
        this.list = list;
        this.uid = i2;
        this.piechart = new WeakReference<>(pie);
        this.barchart = new WeakReference<>(bar);
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
                while(i2 < size) {
                    dailyUsageModel dailyUsageModel = new dailyUsageModel();
                    dailyUsageModel.setStart(this.list.get(i2).getStart());
                    dailyUsageModel.setEnd(this.list.get(i2).getEnd());
                    dailyUsageModel.setType(this.list.get(i2).getType());
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
                    i2++;
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
            this.viewWeakReference.get().setVisibility(View.VISIBLE);
            this.progressBarWeakReference.get().setVisibility(View.INVISIBLE);
            RecyclerView recyclerView = (RecyclerView) this.viewWeakReference.get().findViewById(R.id.data_usage_recyclerView_daily);
            recyclerView.setAdapter(new DataUsageCalculationFragmentAdapter(this.viewWeakReference.get().getContext(), list));
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.viewWeakReference.get().getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            if (this.piechart.get() != null) {
                this.piechart.get().setVisibility(View.VISIBLE);
                this.piechart.get().setUsePercentValues(true);
                this.piechart.get().getDescription().setEnabled(false);
                ArrayList<PieEntry> xvalues = new ArrayList<PieEntry>();
                for (dataUsageIntervalTypeModel m : llist) {
                    xvalues.add(new PieEntry(m.getReceivedWifi() / 1048576.0f, "Wifi Received"));
                    xvalues.add(new PieEntry(m.getSentWifi() / 1048576.0f, "Wifi Sent"));
                    xvalues.add(new PieEntry(m.getReceivedSIM1() / 1048576.0f, "SIM1 Received"));
                    xvalues.add(new PieEntry(m.getSentSIM1() / 1048576.0f, "SIM1 Sent"));

                    if (is2SIM) {
                        xvalues.add(new PieEntry(m.getReceivedSIM2() / 1048576.0f, "SIM2 Received"));
                        xvalues.add(new PieEntry(m.getSentSIM2() / 1048576.0f, "SIM2 Sent"));
                    }
                }
                PieDataSet dataSet = new PieDataSet(xvalues, "");
                dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                PieData data = new PieData(dataSet);
                this.piechart.get().setData(data);
                this.piechart.get().invalidate();
                this.piechart.get().setCenterText("MB\n ");
                this.piechart.get().setDrawEntryLabels(false);
                this.piechart.get().setContentDescription("");
                this.piechart.get().setEntryLabelTextSize(12);
                Legend legend = this.piechart.get().getLegend();
                legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                legend.setWordWrapEnabled(true);
                legend.setDrawInside(false);
                legend.setYOffset(5f);
                legend.setXOffset(20f);
                legend.setYEntrySpace(0f);
                legend.setTextSize(8f);
            }
            if (this.barchart.get() != null) {
                this.barchart.get().setVisibility(View.VISIBLE);
                float groupSpace = 0.1f;
                float barSpace = 0.00f;
                float barWidth = 0.2f;
                int groupCount = llist.size() - 1;
                this.barchart.get().setDrawBarShadow(false);
                this.barchart.get().setFitBars(true);
                this.barchart.get().getDescription().setEnabled(false);
                this.barchart.get().setMaxVisibleValueCount(60);
                this.barchart.get().setPinchZoom(false);
                this.barchart.get().setDrawGridBackground(false);

                ArrayList<BarEntry> values1 = new ArrayList<>();
                ArrayList<BarEntry> values2 = new ArrayList<>();
                ArrayList<BarEntry> values3 = new ArrayList<>();
                ArrayList<BarEntry> values4 = new ArrayList<>();
                ArrayList<BarEntry> values5 = new ArrayList<>();
                ArrayList<BarEntry> values6 = new ArrayList<>();
                int i = 1;
                for (dataUsageIntervalTypeModel m : llist) {
                    values1.add(new BarEntry(i, m.getReceivedWifi() / 1.073741824E9f));
                    values2.add(new BarEntry(i, m.getSentWifi() / 1.073741824E9f));
                    values3.add(new BarEntry(i, m.getReceivedSIM1() / 1.073741824E9f));
                    values4.add(new BarEntry(i, m.getSentSIM1() / 1.073741824E9f));
                    if (is2SIM) {
                        values5.add(new BarEntry(i, m.getReceivedSIM2() / 1.073741824E9f));
                        values6.add(new BarEntry(i, m.getSentSIM2() / 1.073741824E9f));
                    }
                    i++;
                    if(i == llist.size()) break;
                }
                BarDataSet set1 = new BarDataSet(values1, "WiFi Received");
                set1.setColor(Color.rgb(104, 241, 175));
                BarDataSet set2 = new BarDataSet(values2, "WiFi Sent");
                set2.setColor(Color.rgb(164, 228, 251));
                BarDataSet set3 = new BarDataSet(values3, "SIM1 Received");
                set3.setColor(Color.rgb(242, 247, 158));
                BarDataSet set4 = new BarDataSet(values4, "SIM1 Sent");
                set4.setColor(Color.rgb(255, 102, 0));
                BarDataSet set5 = null, set6 = null;
                if (is2SIM) {
                    set5 = new BarDataSet(values5, "SIM2 Received");
                    set5.setColor(Color.rgb(217, 106, 150));
                    set6 = new BarDataSet(values6, "SIM2 Sent");
                    set6.setColor(Color.rgb(230, 124, 124));
                }
                BarData data;
                if (is2SIM) {
                    data = new BarData(set1, set2, set3, set4, set5, set6);
                } else {
                    data = new BarData(set1, set2, set3, set4);
                }
                data.setValueFormatter(new LargeValueFormatter());
                data.setDrawValues(false);
                this.barchart.get().setData(data);
                this.barchart.get().getBarData().setBarWidth(barWidth);
                this.barchart.get().getXAxis().setAxisMinimum(1);
                this.barchart.get().getXAxis().setAxisMaximum(1 + barchart.get().getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
                this.barchart.get().groupBars(1, groupSpace, barSpace);
                this.barchart.get().invalidate();
                this.barchart.get().getAxisRight().setDrawLabels(false);
                this.barchart.get().getAxisLeft().setDrawGridLines(false);
                this.barchart.get().getAxisRight().setDrawGridLines(false);
                this.barchart.get().getAxisRight().setEnabled(false);
                XAxis xAxis = this.barchart.get().getXAxis();
                xAxis.setDrawGridLines(false);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawLabels(false);
                xAxis.setGranularity(1f);
                Legend l = this.barchart.get().getLegend();
                l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                l.setWordWrapEnabled(true);
                l.setDrawInside(false);
                l.setYOffset(5f);
                l.setXOffset(20f);
                l.setYEntrySpace(0f);
                l.setTextSize(8f);
                XYmarkerView mv = new XYmarkerView(this.contextWeakReference.get());
                mv.setChartView(this.barchart.get());
                this.barchart.get().setMarker(mv);
            }
            super.onPostExecute(list);
        }
    }

    public void onPreExecute() {
        this.sharedPreferences = this.contextWeakReference.get().getSharedPreferences("MultipleSim", Context.MODE_PRIVATE);
        this.progressBarWeakReference.get().setVisibility(View.VISIBLE);
        super.onPreExecute();
    }
}