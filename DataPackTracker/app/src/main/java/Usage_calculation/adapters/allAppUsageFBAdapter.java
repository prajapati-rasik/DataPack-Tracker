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

public class allAppUsageFBAdapter extends RecyclerView.Adapter<allAppUsageFBAdapter.allAppUsageFBAdapterView>{

    private List<applicationUIDModel> list;
    private LayoutInflater layoutInflater;
    private applicationUIDModel model;
    private Context context;

    public class allAppUsageFBAdapterView extends RecyclerView.ViewHolder {
        LinearLayout card_lin;
        TextView name;
        TextView percentage;
        TextView background;
        TextView foreground;
        int r;
        applicationUIDModel s;
        ImageView imageView;
        ProgressBar progressBar;
        final allAppUsageFBAdapter adapter;

        allAppUsageFBAdapterView(allAppUsageFBAdapter a, View view) {
            super(view);
            this.adapter = a;
            this.card_lin = (LinearLayout) view.findViewById(R.id.card_lin_fb);
            this.name = (TextView) view.findViewById(R.id.txtApplicationNameFB);
            this.background = (TextView) view.findViewById(R.id.txtBackGroundDataFB);
            this.foreground = (TextView) view.findViewById(R.id.txtForegroundDataFB);
            this.imageView = (ImageView) view.findViewById(R.id.imageappUsageIconFB);
            this.progressBar = (ProgressBar) view.findViewById(R.id.appUsageProgressBarFB);
            this.percentage = (TextView) view.findViewById(R.id.appUsagePercentageFB);
        }

        public void a(applicationUIDModel applicationUIDModel1, int i) {
            this.name.setText(applicationUIDModel1.getLabel());
            this.background.setText(String.format("%s%s%s", this.adapter.context.getString(R.string.background), " ", applicationUIDModel1.getBackgroundData()));
            this.foreground.setText(String.format("%s%s%s", this.adapter.context.getString(R.string.foreground), " ", applicationUIDModel1.getForegroundData()));
            this.percentage.setText(applicationUIDModel1.getPercentage());
            this.progressBar.setProgress(applicationUIDModel1.getPercent());
            Glide.with(this.adapter.context).load(applicationUIDModel1.getUri()).apply( new RequestOptions().centerCrop().placeholder(R.mipmap.ic_launcher)).into(this.imageView);
            this.r = i;
            this.s = applicationUIDModel1;
        }
    }

    public allAppUsageFBAdapter(Context context, List<applicationUIDModel> list) {
        this.list = list;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    @Override
    @NonNull
    public allAppUsageFBAdapterView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new allAppUsageFBAdapterView(this, this.layoutInflater.inflate(R.layout.card_view_fb, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull allAppUsageFBAdapterView view, final int i) {
        this.model = this.list.get(i);
        view.a(this.model, i);
        view.card_lin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(allAppUsageFBAdapter.this.context, appUsageStatistics.class);
                intent.putExtra("Uid", ((applicationUIDModel) allAppUsageFBAdapter.this.list.get(i)).getUid());
                intent.putExtra("AppName", ((applicationUIDModel) allAppUsageFBAdapter.this.list.get(i)).getLabel());
                intent.putExtra("PackageName", ((applicationUIDModel) allAppUsageFBAdapter.this.list.get(i)).getPackageName());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                allAppUsageFBAdapter.this.context.startActivity(intent);
            }
        });
    }
}
