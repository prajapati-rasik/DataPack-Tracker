package appUsageStatistics;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.example.datapacktracker.R;
import com.example.datapacktracker.setLocales;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import Usage_calculation.adapters.DataUsageFragmentPagerAdapter;
import dataUsage.Fragments.DataUsageCustomFragment;
import dataUsage.Fragments.DataUsageDailyFragment;
import dataUsage.Fragments.DataUsageMonthlyFragment;
import dataUsage.Fragments.DataUsageTodayFragment;
import dataUsage.Fragments.DataUsageTotalFragment;
import dataUsage.Fragments.DataUsageWeeklyFragment;
import dataUsage.Fragments.DataUsageYearlyFragment;

public class appUsageStatistics extends AppCompatActivity {

    public Button button;
    private int uid;
    private TextView name;
    private String packageName;
    private ImageView imageView;
    private PackageManager packageManager;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    @Override
    public void attachBaseContext(Context context) {
        super.attachBaseContext(setLocales.setLocale(context));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.app_usage_statistics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.appUsageToolbar);
        toolbar.setTitle(R.string.app_usage);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= 23) {
            toolbar.setTitleTextColor(getResources().getColor(R.color.white, null));
        }
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        this.packageManager = getPackageManager();
        this.name = (TextView) findViewById(R.id.txtAppStatisticAppName);
        this.uid = getIntent().getIntExtra("Uid", 0);
        this.packageName = getIntent().getStringExtra("PackageName");
        this.name.setText(getIntent().getStringExtra("AppName"));
        this.viewPager = (ViewPager2) findViewById(R.id.appStatisticViewPager);
        Bundle bundle2 = new Bundle();
        bundle2.putInt("PackageId", this.uid);

        DataUsageTodayFragment todayFragment = new DataUsageTodayFragment();
        todayFragment.setArguments(bundle2);
        DataUsageDailyFragment dailyFragment = new DataUsageDailyFragment();
        dailyFragment.setArguments(bundle2);
        DataUsageWeeklyFragment weeklyFragment = new DataUsageWeeklyFragment();
        weeklyFragment.setArguments(bundle2);
        DataUsageMonthlyFragment monthlyFragment = new DataUsageMonthlyFragment();
        monthlyFragment.setArguments(bundle2);
        DataUsageYearlyFragment yearlyFragment = new DataUsageYearlyFragment();
        yearlyFragment.setArguments(bundle2);
        DataUsageTotalFragment totalFragment = new DataUsageTotalFragment();
        totalFragment.setArguments(bundle2);
        DataUsageCustomFragment customFragment = new DataUsageCustomFragment();
        customFragment.setArguments(bundle2);

        DataUsageFragmentPagerAdapter adapter = new DataUsageFragmentPagerAdapter(this);
        adapter.addFragment(todayFragment);
        adapter.addFragment(dailyFragment);
        adapter.addFragment(weeklyFragment);
        adapter.addFragment(monthlyFragment);
        adapter.addFragment(yearlyFragment);
        adapter.addFragment(totalFragment);
        adapter.addFragment(customFragment);
        this.viewPager.setAdapter(adapter);

        final String[] fragmentTitles = {"TODAY","DAILY","WEEKLY","MONTHLY","YEARLY","TOTAL","CUSTOM"};
        this.tabLayout = findViewById(R.id.tabs);
        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(fragmentTitles[position]);
                    }
                }
        ).attach();

        this.imageView = (ImageView) findViewById(R.id.appStatisticAppIcon);
        try {
            this.imageView.setImageDrawable(this.packageManager.getApplicationIcon(this.packageName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        this.button = (Button) findViewById(R.id.btnAppStatisticAppInfo);
        this.button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.parse("package:" + appUsageStatistics.this.packageName));
                appUsageStatistics.this.startActivity(intent);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        onBackPressed();
        return true;
    }
}
