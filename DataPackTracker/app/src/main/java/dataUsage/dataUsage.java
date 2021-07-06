package dataUsage;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.example.datapacktracker.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

import Usage_calculation.PermissionChecker;
import Usage_calculation.SubscriberDetails;
import Usage_calculation.adapters.DataUsageFragmentPagerAdapter;
import dataUsage.Fragments.DataUsageCustomFragment;
import dataUsage.Fragments.DataUsageDailyFragment;
import dataUsage.Fragments.DataUsageMonthlyFragment;
import dataUsage.Fragments.DataUsageTodayFragment;
import dataUsage.Fragments.DataUsageTotalFragment;
import dataUsage.Fragments.DataUsageWeeklyFragment;
import dataUsage.Fragments.DataUsageYearlyFragment;

public class dataUsage extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.total_data_usage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.dataUsageToolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        checkPermissions();
        SubscriberDetails.setAllSubscriptionDetails(this, this);

        ViewPager2 viewPager = (ViewPager2) findViewById(R.id.dataUsageViewPager);

        DataUsageFragmentPagerAdapter adapter = new DataUsageFragmentPagerAdapter(this);
        adapter.addFragment(new DataUsageTodayFragment());
        adapter.addFragment(new DataUsageDailyFragment());
        adapter.addFragment(new DataUsageWeeklyFragment());
        adapter.addFragment(new DataUsageMonthlyFragment());
        adapter.addFragment(new DataUsageYearlyFragment());
        adapter.addFragment(new DataUsageTotalFragment());
        adapter.addFragment(new DataUsageCustomFragment());
        viewPager.setAdapter(adapter);

        final String[] fragmentTitles = {"TODAY","DAILY","WEEKLY","MONTHLY","YEARLY","TOTAL","CUSTOM"};
        TabLayout tabLayout = findViewById(R.id.dataUsageTabs);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(fragmentTitles[position])
        ).attach();
    }

    private boolean checkPermissions() {
        if (!PermissionChecker.checkPhoneStatePermission(this, this)) {
            return false;
        } else {
            if(!PermissionChecker.checkUsageAccessPermission(this)){
                PermissionChecker.checkUsageAccessPermissionWithWatchingMode(this, this);
            }
        }
        return PermissionChecker.checkUsageAccessPermission(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        onBackPressed();
        return true;
    }
}
