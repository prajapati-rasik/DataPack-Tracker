package dataUsage;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datapacktracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class floatingButtonScrollBehaviour {

    public static void scrollBehaviour(final FloatingActionButton floatingActionButton, View view) {

        ((RecyclerView) view.findViewById(R.id.data_usage_recyclerView_daily)).addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int i) {
                if (i == 0) {
                    floatingActionButton.show();
                }
                super.onScrollStateChanged(recyclerView, i);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int horizontal, int vertical) {
                if (vertical > 0 || (vertical < 0 && floatingActionButton.isShown())) {
                    floatingActionButton.hide();
                }
            }
        });
    }
}
