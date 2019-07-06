package ml.medyas.wallbay.adapters.pexels;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import ml.medyas.wallbay.R;
import ml.medyas.wallbay.ui.fragments.PexelsViewPagerFragment;

public class PexelsViewPagerAdapter extends FragmentStatePagerAdapter {
    private Resources res;

    public PexelsViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        res = context.getResources();
    }

    @Override
    public Fragment getItem(int i) {
        return PexelsViewPagerFragment.newInstance(i);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return res.getString(R.string.popular);

            case 1:
                return res.getString(R.string.curated);

            default:
                return "";
        }
    }
}
