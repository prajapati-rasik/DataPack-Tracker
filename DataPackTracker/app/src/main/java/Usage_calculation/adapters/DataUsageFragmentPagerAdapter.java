package Usage_calculation.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class DataUsageFragmentPagerAdapter extends FragmentStateAdapter {

    ArrayList<Fragment> fragments = new ArrayList<>();

    public DataUsageFragmentPagerAdapter(FragmentActivity fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return this.fragments.get(position);
    }

    public void addFragment(Fragment fragment){
        this.fragments.add(fragment);
    }

    @Override
    public int getItemCount() {
        return this.fragments.size();
    }
}