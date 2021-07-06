package dataUsage.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datapacktracker.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Usage_calculation.adapters.DataUsageCalculationFragmentAdapter;
import Usage_calculation.asyncTasks.dataUsageCustomAsync;
import Usage_calculation.models.dailyUsageModel;
import Usage_calculation.models.dataUsageAllStringModel;
import Usage_calculation.models.dataUsageIntervalTypeModel;

public class DataUsageCustomFragment extends Fragment {

    private Calendar a;
    private TextView errors;
    private Calendar startCal;
    private Calendar endCal;
    private final DateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");
    private TextView startDate;
    private TextView endDate;
    private ImageView startDateCalender;
    private ImageView endDateCalender;
    private DatePickerDialog datePicker;
    private StringBuilder stringBuilder;
    private RelativeLayout relativeLayout;
    private Calendar b;
    private View view;
    private List<dailyUsageModel> list;
    private dailyUsageModel model;
    private ProgressBar progressBar;
    private dataUsageCustomAsync async;
    private Button button;
    private CardView cardView;

    private boolean showErrors() {
        boolean z;
        Calendar calendar;
        this.stringBuilder = new StringBuilder();
        if (this.startCal == null) {
            this.stringBuilder.append(getResources().getString(R.string.validity_please_enter_valid_start_date));
            this.stringBuilder.append(System.lineSeparator());
            z = false;
        } else {
            z = true;
        }
        if (this.endCal == null) {
            this.stringBuilder.append(getResources().getString(R.string.validity_please_enter_valid_end_date));
            this.stringBuilder.append(System.lineSeparator());
            z = false;
        }
        Calendar calendar2 = this.startCal;
        calendar = this.endCal;
        if (calendar2 == null || calendar == null) {
            return z;
        }
        int compareTo = calendar2.compareTo(calendar);
        if (compareTo == 0) {
            this.stringBuilder.append(getResources().getString(R.string.validity_start_date_and_end_date_should_not_be_equal));
            this.stringBuilder.append(System.lineSeparator());
            return false;
        } else if (1 != compareTo) {
            return z;
        } else {
            this.stringBuilder.append(getResources().getString(R.string.validity_end_date_should_not_be_greater_than_start_date));
            this.stringBuilder.append(System.lineSeparator());
            return false;
        }
    }

    private void showCalender() {
        final Calendar instance = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener r1 = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                DataUsageCustomFragment.this.startCal = Calendar.getInstance();
                DataUsageCustomFragment.this.startCal.set(Calendar.YEAR, i);
                DataUsageCustomFragment.this.startCal.set(Calendar.MONTH, i2);
                DataUsageCustomFragment.this.startCal.set(Calendar.DATE, i3);
                DataUsageCustomFragment.this.startCal.set(Calendar.HOUR_OF_DAY, 0);
                DataUsageCustomFragment.this.startCal.set(Calendar.MINUTE, 1);
                DataUsageCustomFragment.this.startDate.setText(DataUsageCustomFragment.this.dateFormatter.format(DataUsageCustomFragment.this.startCal.getTime()));
                DataUsageCustomFragment.this.startCal.add(Calendar.MINUTE, -10);
            }
        };
        this.startDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new DatePickerDialog(DataUsageCustomFragment.this.getActivity(), r1, instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DATE)).show();
            }
        });
        this.startDateCalender.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new DatePickerDialog(DataUsageCustomFragment.this.getActivity(), r1, instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DATE)).show();
            }
        });
        final DatePickerDialog.OnDateSetListener r2 = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                DataUsageCustomFragment.this.endCal = Calendar.getInstance();
                DataUsageCustomFragment.this.endCal.set(Calendar.YEAR, i);
                DataUsageCustomFragment.this.endCal.set(Calendar.MONTH, i2);
                DataUsageCustomFragment.this.endCal.set(Calendar.DATE, i3);
                DataUsageCustomFragment.this.endCal.set(Calendar.HOUR_OF_DAY, 23);
                DataUsageCustomFragment.this.endCal.set(Calendar.MINUTE, 59);
                DataUsageCustomFragment.this.endDate.setText(DataUsageCustomFragment.this.dateFormatter.format(DataUsageCustomFragment.this.endCal.getTime()));
            }
        };
        this.endDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new DatePickerDialog(DataUsageCustomFragment.this.getActivity(), r2, instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DATE)).show();
            }
        });
        this.endDateCalender.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DataUsageCustomFragment dataUsageCustomFragment = DataUsageCustomFragment.this;
                dataUsageCustomFragment.datePicker = new DatePickerDialog(dataUsageCustomFragment.getActivity(), r2, instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DATE));
                dataUsageCustomFragment.datePicker.show();
            }
        });
    }

    private void setView(View view) {
        this.startDate = (TextView) view.findViewById(R.id.txtViewSelectStartDateCustom);
        this.progressBar = (ProgressBar) view.findViewById(R.id.dataUsageMProgressBarCustom);
        this.button = (Button) view.findViewById(R.id.btnGetDataUsageCustom);
        this.endDate = (TextView) view.findViewById(R.id.txtViewSelectEndDateCustom);
        this.relativeLayout = (RelativeLayout) view.findViewById(R.id.containerRelativeLayoutCustom);
        this.cardView = (CardView) view.findViewById(R.id.errorsCardViewCustom);
        this.errors = (TextView) view.findViewById(R.id.txtErrorsCustom);
        this.startDateCalender = (ImageView) view.findViewById(R.id.imageStartDateCalenderCustom);
        this.endDateCalender = (ImageView) view.findViewById(R.id.imageEndDateCalenderCustom);
    }

    public static void updateRecycleView(View view, List<dataUsageAllStringModel> list) {
        if (view != null && view.getContext() != null) {
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.data_usage_recyclerView_custom);
            recyclerView.setAdapter(new DataUsageCalculationFragmentAdapter(view.getContext(), list));
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }
    }

    public static void updateChart(View view, List<dataUsageIntervalTypeModel> list, boolean isDual){
        PieChart pie = (PieChart)view.findViewById(R.id.PiechartCustom);
        pie.setVisibility(View.VISIBLE);
        pie.setUsePercentValues(true);
        pie.getDescription().setEnabled(false);
        ArrayList<PieEntry> xvalues = new ArrayList<PieEntry>();
        for(dataUsageIntervalTypeModel m : list){
            xvalues.add(new PieEntry(m.getReceivedWifi()/1048576.0f, "Wifi Received"));
            xvalues.add(new PieEntry(m.getSentWifi()/1048576.0f, "Wifi Sent"));
            xvalues.add(new PieEntry(m.getReceivedSIM1()/1048576.0f, "SIM1 Received"));
            xvalues.add(new PieEntry(m.getSentSIM1()/1048576.0f, "SIM1 Sent"));
            if(isDual){
                xvalues.add(new PieEntry(m.getReceivedSIM2()/1048576.0f, "SIM2 Received"));
                xvalues.add(new PieEntry(m.getSentSIM2()/1048576.0f, "SIM2 Sent"));
            }
        }
        PieDataSet dataSet = new PieDataSet(xvalues, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData data = new PieData(dataSet);
        pie.setData(data);
        pie.invalidate();
        pie.setCenterText("MB\n ");
        pie.setDrawEntryLabels(false);
        pie.setContentDescription("");
        pie.setEntryLabelTextSize(12);
        Legend legend = pie.getLegend();
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

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        try {
            this.view = layoutInflater.inflate(R.layout.custom_data_usage_fragment, viewGroup, false);
            setView(this.view);
            showCalender();
            this.button.setOnClickListener(new View.OnClickListener() {
                @SuppressLint({"WrongConstant"})
                public void onClick(View view) {
                    if (DataUsageCustomFragment.this.showErrors()) {
                        DataUsageCustomFragment.this.cardView.setVisibility(View.GONE);
                        DataUsageCustomFragment.this.list = new ArrayList<>();
                        DataUsageCustomFragment.this.model = new dailyUsageModel();
                        DataUsageCustomFragment.this.a = DataUsageCustomFragment.this.startCal;
                        DataUsageCustomFragment.this.a.add(Calendar.HOUR_OF_DAY, -1);
                        DataUsageCustomFragment.this.b = DataUsageCustomFragment.this.endCal;
                        DataUsageCustomFragment.this.b.add(Calendar.HOUR_OF_DAY, 2);
                        DataUsageCustomFragment.this.model.setStart(DataUsageCustomFragment.this.a);
                        DataUsageCustomFragment.this.model.setEnd(DataUsageCustomFragment.this.b);
                        dailyUsageModel dailyUsageModel = DataUsageCustomFragment.this.model;
                        dailyUsageModel.setType(DataUsageCustomFragment.this.dateFormatter.format(DataUsageCustomFragment.this.startCal.getTime()) + " " + DataUsageCustomFragment.this.getResources().getString(R.string.to) + " " + DataUsageCustomFragment.this.dateFormatter.format(DataUsageCustomFragment.this.endCal.getTime()));
                        DataUsageCustomFragment.this.list.add(DataUsageCustomFragment.this.model);
                        if (DataUsageCustomFragment.this.getArguments() != null) {
                            DataUsageCustomFragment fragment3 = DataUsageCustomFragment.this;
                            fragment3.async = new dataUsageCustomAsync(fragment3.view, DataUsageCustomFragment.this.getActivity().getApplicationContext(), DataUsageCustomFragment.this.progressBar, DataUsageCustomFragment.this.list, DataUsageCustomFragment.this.getArguments().getInt("PackageId", 0));
                            DataUsageCustomFragment.this.async.execute();
                        } else {
                            DataUsageCustomFragment fragment4 = DataUsageCustomFragment.this;
                            fragment4.async = new dataUsageCustomAsync(fragment4.view, DataUsageCustomFragment.this.getActivity().getApplicationContext(), DataUsageCustomFragment.this.progressBar, DataUsageCustomFragment.this.list, 0);
                            DataUsageCustomFragment.this.async.execute();
                        }
                        DataUsageCustomFragment.this.a = null;
                        DataUsageCustomFragment.this.b = null;
                        return;
                    }else {
                        DataUsageCustomFragment.this.cardView.setVisibility(View.VISIBLE);
                        DataUsageCustomFragment.this.errors.setText(DataUsageCustomFragment.this.stringBuilder);
                    }
                }
            });
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return this.view;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroy() {
        dataUsageCustomAsync async = this.async;
        if (async != null) {
            async.cancel(true);
            this.async = null;
        }
        this.startCal = null;
        this.endCal = null;
        super.onDestroy();
    }
}
