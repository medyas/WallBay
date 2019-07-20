package ml.medyas.wallbay.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.transition.Fade;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

import io.reactivex.Completable;
import ml.medyas.wallbay.R;
import ml.medyas.wallbay.databinding.ActivityMainBinding;
import ml.medyas.wallbay.databinding.NavigationBaseLayoutBinding;
import ml.medyas.wallbay.models.ImageEntity;
import ml.medyas.wallbay.ui.fragments.AboutFragment;
import ml.medyas.wallbay.ui.fragments.BaseFragment;
import ml.medyas.wallbay.ui.fragments.FavoriteFragment;
import ml.medyas.wallbay.ui.fragments.ForYouFragment;
import ml.medyas.wallbay.ui.fragments.GetStartedFragment;
import ml.medyas.wallbay.ui.fragments.ImageDetailsFragment;
import ml.medyas.wallbay.ui.fragments.ImageDetailsInfoDialog;
import ml.medyas.wallbay.ui.fragments.PexelsFragment;
import ml.medyas.wallbay.ui.fragments.PixabayFragment;
import ml.medyas.wallbay.ui.fragments.PixabayViewPagerFragment;
import ml.medyas.wallbay.ui.fragments.SearchFragment;
import ml.medyas.wallbay.ui.fragments.UnsplashCollectionsFragment;
import ml.medyas.wallbay.ui.fragments.UnsplashDefaultVPFragment;
import ml.medyas.wallbay.ui.fragments.UnsplashFragment;
import ml.medyas.wallbay.utils.DetailsTransition;
import ml.medyas.wallbay.viewmodels.FavoriteViewModel;
import ml.medyas.wallbay.widget.WallbayWidget;

import static ml.medyas.wallbay.utils.Utils.INTEREST_CATEGORIES;

public class MainActivity extends AppCompatActivity implements GetStartedFragment.OnGetStartedFragmentInteractions, BaseFragment.OnBaseFragmentInteractions,
        ImageDetailsFragment.OnImageDetailsFragmentInteractions, FavoriteFragment.onFavoriteFragmentInteractions, PixabayFragment.OnPixabayFragmentInteractions,
        UnsplashFragment.OnUnsplashFragmentInteractions, PexelsFragment.OnPexelsFragmentInteractions, SearchFragment.OnSearchFragmentInteractions,
        AboutFragment.OnFragmentInteractionListener, ForYouFragment.OnForYouFragmentInteractions, UnsplashCollectionsFragment.UnsplashCollectionInterface,
        ImageDetailsInfoDialog.OnImageDialogInteractions {

    public static final String FIRST_START = "first_start";
    public static final String TOOLBAR_VISIBILITY = "toolbar_visibility";
    public static final String FAVORITE_SHOWN = "FAVORITE_SHOWN";
    public static final String FRAGMENT_STACK = "FRAGMENT_STACK";
    public static final String IMAGE_ITEM = "IMAGE_ITEM";
    public static final String LAUNCH_IMAGE_DETAILS = "MainActivity.LAUNCH_IMAGE_DETAILS";
    public static final String LAUNCH_APP = "MainActivity.LAUNCH_APP";
    public static final String CAPSTONE_PROJECT = "https://github.com/medyas/Capstone-Project";
    public static final String CATEGORIES = "categories";

    private ActivityMainBinding binding;
    private FavoriteViewModel favoriteViewModel;
    private boolean addedFragmentShown = false;
    private int fragmentStack =  0;
    private FirebaseAnalytics mFirebaseAnalytics;

    //TODO: add slideshow - favorite images wallpaper slide - relaxing audio
    // TODO: migrate to and Kotlin

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);
        WallbayWidget.sendRefreshBroadcast(this);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this);

        setSupportActionBar(binding.content.toolbar);
        setUpToolbar(true);

        if (savedInstanceState == null) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            if (pref.getBoolean(FIRST_START, true)) {
                binding.startContainer.setVisibility(View.VISIBLE);
                showStartFragment();
                lockDrawer(true);
            } else {
                binding.startContainer.setVisibility(View.GONE);
                binding.content.coordinator.setVisibility(View.VISIBLE);
                replaceFragment(ForYouFragment.newInstance(), false);
                getSupportActionBar().setTitle(getResources().getString(R.string.for_you));
            }
            showToolbar(true);
        } else {
            binding.content.coordinator.setVisibility(View.VISIBLE);
            showToolbar(savedInstanceState.getBoolean(TOOLBAR_VISIBILITY));
            setUpToolbar(!savedInstanceState.getBoolean(FAVORITE_SHOWN));
            fragmentStack = savedInstanceState.getInt(FRAGMENT_STACK);
        }

        binding.navigation.setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub viewStub, View view) {
                NavigationBaseLayoutBinding b = DataBindingUtil.bind(view);
                b.setActivity(MainActivity.this);
            }
        });

        checkIntent(getIntent().getAction());

        binding.drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {
                if(!binding.navigation.isInflated()) {
                    binding.navigation.getViewStub().inflate();
                }
            }

            @Override
            public void onDrawerOpened(@NonNull View view) {

            }

            @Override
            public void onDrawerClosed(@NonNull View view) {

            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });

        binding.content.admobLayout.closeAdmob.setVisibility(View.GONE);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("94956D314FBB41CFF09E4CE159A1A73E").build();
        binding.content.admobLayout.adView.loadAd(adRequest);
        binding.content.admobLayout.adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                binding.content.admobLayout.closeAdmob.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });
        binding.content.admobLayout.closeAdmob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.content.admobLayout.admobContainer.setVisibility(View.GONE);
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        checkIntent(intent.getAction());
    }

    private void checkIntent(String action) {
        if(action != null) {

            if(action.equals(LAUNCH_IMAGE_DETAILS)) {
                ImageEntity imgItem = getIntent().getParcelableExtra(IMAGE_ITEM);
                displayImageDetails(imgItem);
            }
        }
    }

    private void displayImageDetails(ImageEntity imageEntity) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_container);
        if(fragment instanceof ImageDetailsFragment) {
            onBackPressed();
        }

        onItemClicked(imageEntity, 0, null);
    }

    private void setUpToolbar(final boolean setup) {
        if (setup) {
            addedFragmentShown = false;
            binding.content.toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);
            binding.content.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!binding.navigation.isInflated()) {
                        binding.navigation.getViewStub().inflate();
                    }
                    binding.drawerLayout.openDrawer(GravityCompat.START);
                }
            });
        } else {
            addedFragmentShown = true;
            binding.content.toolbar.getMenu().clear();
            binding.content.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            binding.content.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }

        lockDrawer(!setup);

        findToolbarTitle();
    }

    private void showToolbar(boolean show) {
        if (show) {
            getSupportActionBar().show();
        } else {
            getSupportActionBar().hide();
            lockDrawer(true);
        }
    }

    private void lockDrawer(boolean lock) {
        if(lock) {
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(TOOLBAR_VISIBILITY, binding.content.toolbar.getVisibility() == View.VISIBLE);
        outState.putBoolean(FAVORITE_SHOWN, addedFragmentShown);

        if(fragmentStack < 0)
            fragmentStack = 0;
        outState.putInt(FRAGMENT_STACK, fragmentStack);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment  = getSupportFragmentManager().findFragmentById(R.id.main_container);
        super.onBackPressed();
        fragmentStack-=1;

        if (fragment instanceof ImageDetailsFragment) {
            showToolbar(true);
        } else if (fragment instanceof FavoriteFragment ||
                        fragment instanceof PixabayViewPagerFragment ||
                        fragment instanceof UnsplashDefaultVPFragment ) {
            if(fragmentStack == 0) {
                setUpToolbar(addedFragmentShown);
            }
        }

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Fragment frag = getSupportFragmentManager().findFragmentById(R.id.main_container);
                setToolbarElevation(frag);
                binding.content.toolbar.getMenu().clear();
                if(frag instanceof PixabayFragment || frag instanceof UnsplashFragment || frag instanceof PexelsFragment) {
                    binding.content.toolbar.inflateMenu(R.menu.search_menu);
                    setUpToolbar(true);
                } else if(frag instanceof SearchFragment || frag instanceof ForYouFragment || frag instanceof AboutFragment){
                    binding.content.toolbar.inflateMenu(R.menu.main_activity_menu);
                    setUpToolbar(true);
                }
            }
        }, 10);
    }

    private void findToolbarTitle() {
        if(getSupportFragmentManager().findFragmentById(R.id.main_container) != null) {
            getSupportActionBar().setTitle(getToolbarTitle(getSupportFragmentManager().findFragmentById(R.id.main_container)));
        }
    }

    private Fragment setToolbarTitle(Fragment fragment) {
        getSupportActionBar().setTitle(getToolbarTitle(fragment));
        return fragment;
    }

    private String getToolbarTitle(Fragment fragment) {
        if(fragment instanceof ForYouFragment)
            return getResources().getString(R.string.for_you);

        else if(fragment instanceof PixabayFragment)
            return getResources().getString(R.string.pixabay);

        else if(fragment instanceof UnsplashFragment)
            return getResources().getString(R.string.unsplash);

        else if(fragment instanceof PexelsFragment)
            return getResources().getString(R.string.pexels);

        else if(fragment instanceof SearchFragment)
            return getResources().getString(R.string.search);

        else if(fragment instanceof AboutFragment)
            return getResources().getString(R.string.about);

        else if(fragment instanceof FavoriteFragment)
            return getResources().getString(R.string.favorite);

        else
            return "";
    }

    private void setToolbarElevation(Fragment fragment) {
        if(fragment instanceof PixabayFragment || fragment instanceof UnsplashFragment || fragment instanceof PexelsFragment) {
            ViewCompat.setElevation(binding.content.appBar, 0);
        } else {
            ViewCompat.setElevation(binding.content.appBar, 4 * getResources().getDisplayMetrics().density);
        }
    }

    private void showStartFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.start_container, GetStartedFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }


    private void replaceFragment(Fragment fragment, boolean replace) {
        if (getSupportFragmentManager().findFragmentByTag(fragment.getClass().getName()) == null || replace) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                fragment.setSharedElementReturnTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.slide_right));
                fragment.setExitTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.slide_left));
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, setToolbarTitle(fragment), fragment.getClass().getName())
                    .commit();

        }

        setToolbarElevation(fragment);
    }

    public void onNavItemClicked(View view) {
        int id = view.getId();
        Intent shareIntent;
        switch (id) {
            case R.id.nav_for_you:
                replaceFragment(ForYouFragment.newInstance(), false);
                break;

            case R.id.nav_pixabay:
                replaceFragment(PixabayFragment.newInstance(), false);
                break;

            case R.id.nav_unsplash:
                replaceFragment(UnsplashFragment.newInstance(), false);
                break;

            case R.id.nav_pexels:
                replaceFragment(PexelsFragment.newInstance(), false);
                break;

            case R.id.nav_search:
                replaceFragment(SearchFragment.newInstance(), false);
                break;

            case R.id.nav_share:
                shareIntent = new Intent(Intent.ACTION_VIEW);
                shareIntent.setData(Uri.parse(CAPSTONE_PROJECT));
                startActivity(shareIntent);
                break;

            case R.id.nav_rate:
                shareIntent = new Intent(Intent.ACTION_VIEW);
                shareIntent.setData(Uri.parse(CAPSTONE_PROJECT));
                startActivity(shareIntent);
                break;

            case R.id.nav_about:
                replaceFragment(AboutFragment.newInstance(), false);
                break;
        }

        binding.content.admobLayout.admobContainer.setVisibility(View.VISIBLE);
        binding.drawerLayout.closeDrawers();
    }

    @Override
    public void onGetStartedDone(String categories) {
        binding.startContainer.setVisibility(View.GONE);

        SharedPreferences.Editor pref = PreferenceManager.getDefaultSharedPreferences(this).edit();
        pref.putBoolean(FIRST_START, false);
        pref.putString(INTEREST_CATEGORIES, categories);
        pref.apply();

        binding.content.coordinator.setVisibility(View.VISIBLE);
        lockDrawer(false);

        replaceFragment(ForYouFragment.newInstance(), false);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.start_container);
        if(fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                    .commit();
        }

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, CATEGORIES);
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, categories);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    @Override
    public void reCreateFragment(Fragment fragment) {
        replaceFragment(fragment, true);
    }

    @Override
    public Completable onAddToFavorite(ImageEntity imageEntity) {
        WallbayWidget.sendRefreshBroadcast(this);
        return favoriteViewModel.insertFavorite(imageEntity);
    }

    @Override
    public Completable onAddToFavorite(List<ImageEntity> imageEntities) {
        WallbayWidget.sendRefreshBroadcast(this);
        return favoriteViewModel.insertFavorite(imageEntities);
    }

    @Override
    public void onItemClicked(ImageEntity imageEntity, int position, ImageView imageView) {
        ImageDetailsFragment frag = ImageDetailsFragment.newInstance(imageEntity);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            frag.setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.move));
            //frag.setEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.slide_right));
            frag.setExitTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.move));
        }*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            frag.setSharedElementEnterTransition(new DetailsTransition());
            frag.setSharedElementReturnTransition(new DetailsTransition());
            frag.setEnterTransition(new Fade());
//            setExitTransition(new Fade());
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .add(R.id.main_container, frag, frag.getClass().getName())
                .addToBackStack(frag.getClass().getName());

        if(imageView != null) {
            transaction.addSharedElement(imageView, String.format("transition-%s", imageEntity.getId()));
        }
        transaction.commit();

        showToolbar(false);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, imageEntity.getId());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, imageEntity.getProvider().name());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    @Override
    public void onAddFragment(Fragment fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.setSharedElementReturnTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.slide_right));
            fragment.setExitTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.slide_left));
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_container, fragment, fragment.getClass().getName())
                .addToBackStack(fragment.getClass().getName())
                .commit();

        addedFragmentShown = true;
        setUpToolbar(false);
        fragmentStack+=1;

        if(fragment instanceof FavoriteFragment) {
            getSupportActionBar().setTitle(getString(R.string.favorite));
        }

        setToolbarElevation(fragment);
    }

    @Override
    public void onTagItemPressed(Fragment fragment, String query) {
        onBackPressed();

        onAddFragment(fragment);
        updateToolbarTitle(query);
    }

    @Override
    public void updateToolbarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onImageBackPressed() {
        onBackPressed();
    }
}
