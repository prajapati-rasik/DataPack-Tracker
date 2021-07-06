package dataUsage.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.example.datapacktracker.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import Usage_calculation.asyncTasks.appUsageAsync;
import Usage_calculation.asyncTasks.dataUsageAsync;
import Usage_calculation.asyncTasks.dataUsageCustomAsync;
import Usage_calculation.makeCalenderInstances;
import dataUsage.floatingButtonScrollBehaviour;

public class DataUsageYearlyFragment extends Fragment {

    private ProgressBar progressBar;
    private View view;
    private dataUsageAsync asyncTask;
    private appUsageAsync customAsync;

    private void setData(View view, PieChart pie, BarChart bar) {
        if (getActivity() != null) {
            if(getArguments() == null) {
                this.asyncTask = new dataUsageAsync(getActivity().getApplicationContext(), view, this.progressBar, makeCalenderInstances.years(getActivity().getApplicationContext()), pie, bar);
                this.asyncTask.execute();
            }else{
                this.customAsync = new appUsageAsync(view, getActivity().getApplicationContext(), this.progressBar, makeCalenderInstances.years(getActivity().getApplicationContext()), getArguments().getInt("PackageId", 0), pie, bar);
                this.customAsync.execute();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view = layoutInflater.inflate(R.layout.daily_data_usage_fragment, viewGroup, false);
        this.progressBar = (ProgressBar) this.view.findViewById(R.id.dataUsageProgressBarDaily);
        BarChart chart = this.view.findViewById(R.id.BarchartDaily);
        setData(this.view, null, chart);
        FloatingActionButton floatingActionButton = (FloatingActionButton) this.view.findViewById(R.id.datUsageMFabDaily);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DataUsageYearlyFragment secondView = DataUsageYearlyFragment.this;
                secondView.setData(secondView.view, null, chart);
            }
        });
        floatingButtonScrollBehaviour.scrollBehaviour(floatingActionButton, view);
        return this.view;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroy() {
        dataUsageAsync async = this.asyncTask;
        if (async != null) {
            async.cancel(true);
            this.asyncTask = null;
        }
        appUsageAsync casync = this.customAsync;
        if(casync != null){
            casync.cancel(true);
            this.customAsync = null;
        }
        super.onDestroy();
    }
}
