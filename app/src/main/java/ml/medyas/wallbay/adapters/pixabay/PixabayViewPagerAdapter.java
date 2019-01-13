package ml.medyas.wallbay.adapters.pixabay;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ml.medyas.wallbay.R;
import ml.medyas.wallbay.ui.fragments.PixabayViewPagerFragment;

public class PixabayViewPagerAdapter extends FragmentStatePagerAdapter {
    private Resources res;

    public PixabayViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        res = context.getResources();
    }

    @Override
    public Fragment getItem(int i) {
        return PixabayViewPagerFragment.newInstance(i);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return res.getString(R.string.popular);
            case 1:
                return res.getString(R.string.latest);
            case 2:
                return res.getString(R.string.editors_choice);
        }
        return "";
    }
}
