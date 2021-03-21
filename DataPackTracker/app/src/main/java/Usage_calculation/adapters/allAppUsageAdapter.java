package Usage_calculation.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.datapacktracker.R;

import java.util.List;

import Usage_calculation.models.applicationUIDModel;
import appUsageStatistics.appUsageStatistics;

public class allAppUsageAdapter extends RecyclerView.Adapter<allAppUsageAdapter.allAppUsageAdapterView> {
    public static boolean firstfb1 = false;
    private List<applicationUIDModel> list;
    private LayoutInflater layoutInflater;
    private applicationUIDModel model;
    private Context context;

    public class allAppUsageAdapterView extends RecyclerView.ViewHolder {
        LinearLayout card_lin1;
        TextView name;
        TextView data;
        TextView percentage;
        int q;
        applicationUIDModel r;
        ImageView icon;
        ProgressBar progressBar;
        final allAppUsageAdapter adapter;

        allAppUsageAdapterView(allAppUsageAdapter adapter1, View view) {
            super(view);
            this.adapter = adapter1;
            this.card_lin1 = (LinearLayout) view.findViewById(R.id.card_lin1);
            this.name = (TextView) view.findViewById(R.id.txtApplicationName);
            this.data = (TextView) view.findViewById(R.id.txtAppUsageData);
            this.icon = (ImageView) view.findViewById(R.id.imageappUsageIcon);
            this.progressBar = (ProgressBar) view.findViewById(R.id.appUsageProgressBar);
            this.percentage = (TextView) view.findViewById(R.id.appUsagePercentage);
        }

        public void setView(applicationUIDModel applicationUIDModel1, int i) {
            this.name.setText(applicationUIDModel1.getLabel());
            this.data.setText(applicationUIDModel1.getFormattedData());
            this.percentage.setText(applicationUIDModel1.getPercentage());
            this.progressBar.setProgress(applicationUIDModel1.getPercent());
            Glide.with(this.adapter.context).load(applicationUIDModel1.getUri()).apply( new RequestOptions().centerCrop().placeholder(R.mipmap.ic_launcher)).into(this.icon);
            this.q = i;
            this.r = applicationUIDModel1;
        }
    }

    public allAppUsageAdapter(Context context, List<applicationUIDModel> list) {
        this.list = list;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    @NonNull
    public allAppUsageAdapterView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new allAppUsageAdapterView(this, this.layoutInflater.inflate(R.layout.card_view_fb, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull allAppUsageAdapterView allAppUsageAdapterView2, final int i) {
        this.model = this.list.get(i);
        allAppUsageAdapterView2.setView(this.model, i);
        allAppUsageAdapterView2.card_lin1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                allAppUsageAdapter.firstfb1 = true;
                Intent intent = new Intent(allAppUsageAdapter.this.context, appUsageStatistics.class);
                intent.putExtra("Uid", ((applicationUIDModel) allAppUsageAdapter.this.list.get(i)).getUid());
                intent.putExtra("AppName", ((applicationUIDModel) allAppUsageAdapter.this.list.get(i)).getLabel());
                intent.putExtra("PackageName", ((applicationUIDModel) allAppUsageAdapter.this.list.get(i)).getPackageName());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                allAppUsageAdapter.this.context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }
}
