package ml.medyas.wallbay.adapters.unsplash;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ml.medyas.wallbay.R;
import ml.medyas.wallbay.ui.fragments.UnsplashCollectionsFragment;
import ml.medyas.wallbay.ui.fragments.UnsplashDefaultVPFragment;

public class UnsplashViewPagerAdapter extends FragmentStatePagerAdapter {
    private Resources res;

    public UnsplashViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.res = context.getResources();
    }

    @Override
    public Fragment getItem(int i) {
        if(i==3 || i==4) {
            return UnsplashCollectionsFragment.newInstance(i);

        } else {
            return UnsplashDefaultVPFragment.newInstance(i);
        }
    }

    @Override
    public int getCount() {
        return 5;
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
                return res.getString(R.string.oldest);
            case 3:
                return res.getString(R.string.collections);
            case 4:
                return res.getString(R.string.featured);
        }
        return "";
    }
}
