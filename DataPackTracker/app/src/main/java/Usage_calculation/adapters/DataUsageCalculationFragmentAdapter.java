package Usage_calculation.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datapacktracker.R;

import java.util.List;

import Usage_calculation.models.dataUsageAllStringModel;

public class DataUsageCalculationFragmentAdapter extends RecyclerView.Adapter<DataUsageCalculationFragmentAdapter.DataUsageTodayFragmentAdapterView> {

    private List<dataUsageAllStringModel> list;
    private LayoutInflater layoutInflater;
    private Context context;
    private SharedPreferences sharedPreferences;
    private boolean isDualSim ;
    private String sim1 ;
    private String sim2 ;

    public DataUsageCalculationFragmentAdapter(Context context, List<dataUsageAllStringModel> list) {
        this.list = list;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("MultipleSim", 0);
    }

    @NonNull
    @Override
    public DataUsageTodayFragmentAdapterView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.isDualSim = this.sharedPreferences.getBoolean("isDualSim", false);
        this.sim1 = this.sharedPreferences.getString("simName1", "SIM 1");
        this.sim2 = this.sharedPreferences.getString("simName2", "SIM 2");
        View inflate = this.layoutInflater.inflate(R.layout.dual_sim_data_usage, parent, false);
        if (!this.isDualSim) {
            inflate.findViewById(R.id.dualSimVisibilityDS).setVisibility(View.INVISIBLE);
        }
        return new DataUsageTodayFragmentAdapterView(this, inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull DataUsageTodayFragmentAdapterView holder, int position) {
        holder.setData(this.list.get(position), position);
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public static class DataUsageTodayFragmentAdapterView extends RecyclerView.ViewHolder {

        DataUsageCalculationFragmentAdapter dataUsageTodayFragmentAdapter;
        dataUsageAllStringModel model;
        TextView title;
        TextView wifiRx;
        TextView wifiTx;
        TextView wifiTotal;
        TextView sim1Rx;
        TextView sim1Tx;
        TextView sim1Total;
        TextView sim2Rx;
        TextView sim2Tx;
        TextView sim2Total;
        TextView sim1;
        TextView sim2;
        int position;

        public DataUsageTodayFragmentAdapterView(DataUsageCalculationFragmentAdapter dataUsageTodayFragmentAdapter, @NonNull View itemView) {
            super(itemView);
            this.dataUsageTodayFragmentAdapter = dataUsageTodayFragmentAdapter;
            this.title = (TextView) itemView.findViewById(R.id.txtDataUsageMTitleDS);
            this.wifiRx = (TextView) itemView.findViewById(R.id.txtDataUsageMWifiRxDS);
            this.wifiTx = (TextView) itemView.findViewById(R.id.txtDataUsageMWifiTxDS);
            this.wifiTotal = (TextView) itemView.findViewById(R.id.txtDataUsageMWifiTotalDS);
            this.sim1Rx = (TextView) itemView.findViewById(R.id.txtDataUsageMSimOneRxDS);
            this.sim1Tx = (TextView) itemView.findViewById(R.id.txtDataUsageMSimOneTxDS);
            this.sim1Total = (TextView) itemView.findViewById(R.id.txtDataUsageMSimOneTotalDS);
            this.sim2Rx = (TextView) itemView.findViewById(R.id.txtDataUsageMSimTwoRxDS);
            this.sim2Tx = (TextView) itemView.findViewById(R.id.txtDataUsageMSimTwoTxDS);
            this.sim2Total = (TextView) itemView.findViewById(R.id.txtDataUsageMSimTwoTotalDS);
            this.sim1 = (TextView) itemView.findViewById(R.id.txtDataUsageMSimOneNameDS);
            this.sim2 = (TextView) itemView.findViewById(R.id.txtDataUsageMSimTwoNameDS);
        }

        public void setData(dataUsageAllStringModel model, int i) {
            this.title.setText(model.getTitle());
            this.wifiRx.setText(model.getWifiRx());
            this.wifiTx.setText(model.getWifiTx());
            this.wifiTotal.setText(model.getWifiTotal());
            this.sim1Rx.setText(model.getSim1Rx());
            this.sim1Tx.setText(model.getSim1Tx());
            this.sim1Total.setText(model.getSim1Total());
            this.sim1.setText(this.dataUsageTodayFragmentAdapter.sim1);
            if (this.dataUsageTodayFragmentAdapter.isDualSim) {
                this.sim2.setText(this.dataUsageTodayFragmentAdapter.sim2);
                this.sim2Rx.setText(model.getSim2Rx());
                this.sim2Tx.setText(model.getSim2Tx());
                this.sim2Total.setText(model.getSim2Total());
            }
            this.position = i;
            this.model = model;
        }
    }
}
