package ml.medyas.wallbay.ui.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionInflater;
import android.view.Gravity;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;

import java.util.List;

import io.reactivex.Completable;
import ml.medyas.wallbay.R;
import ml.medyas.wallbay.databinding.ActivityMainBinding;
import ml.medyas.wallbay.databinding.NavigationBaseLayoutBinding;
import ml.medyas.wallbay.entities.ImageEntity;
import ml.medyas.wallbay.models.FavoriteViewModel;
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

    private ActivityMainBinding binding;
    private FavoriteViewModel favoriteViewModel;
    private boolean addedFragmentShown = false;
    private int fragmentStack =  0;

    //TODO 1: Create fragments UI : For You
    //• Pixabay
    //• Pexels
    //• Unsplash
    //• Search


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);

        setSupportActionBar(binding.content.toolbar);
        setUpToolbar(true);

        if (savedInstanceState == null) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            if (pref.getBoolean(FIRST_START, true)) {
                binding.startContainer.setVisibility(View.VISIBLE);
                showStartFragment();
            } else {
                binding.content.coordinator.setVisibility(View.VISIBLE);
                replaceFragment(ForYouFragment.newInstance(), false);
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

    }

    @Override
    protected void onResume() {
        if(!binding.navigation.isInflated()) {
            binding.navigation.getViewStub().inflate();
        }
        super.onResume();
    }

    private void setUpToolbar(final boolean setup) {
        if (setup) {
            addedFragmentShown = false;
            binding.content.toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);
            binding.content.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.drawerLayout.openDrawer(Gravity.START);
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

        findToolbarTitle();
    }

    private void showToolbar(boolean show) {
        if (show) {
            getSupportActionBar().show();
        } else {
            getSupportActionBar().hide();
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
        String className = fragment.getClass().getName();

        super.onBackPressed();
        fragmentStack-=1;

        if (className.equals(ImageDetailsFragment.TAG)) {
            showToolbar(true);
        } else if (className.equals(FavoriteFragment.TAG) ||
                        className.equals(PixabayViewPagerFragment.TAG) ||
                        className.equals(UnsplashDefaultVPFragment.TAG) ) {
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
            String fragment = getSupportFragmentManager().findFragmentById(R.id.main_container).getClass().getName();
            getSupportActionBar().setTitle(getToolbarTitle(fragment));
        }
    }

    private Fragment setToolbarTitle(Fragment fragment) {
        getSupportActionBar().setTitle(getToolbarTitle(fragment.getClass().getName()));
        return fragment;
    }

    private String getToolbarTitle( String name) {
        switch (name) {
            case ForYouFragment.TAG:
                return getResources().getString(R.string.for_you);

            case PixabayFragment.TAG:
                return getResources().getString(R.string.pixabay);

            case UnsplashFragment.TAG:
                return getResources().getString(R.string.unsplash);

            case PexelsFragment.TAG:
                return getResources().getString(R.string.pexels);

            case SearchFragment.TAG:
                return getResources().getString(R.string.search);

            case AboutFragment.TAG:
                return getResources().getString(R.string.about);

            case FavoriteFragment.TAG:
                return getResources().getString(R.string.favorite);
        }
        return "";
    }

    private void setToolbarElevation(Fragment fragment) {
        if(fragment instanceof PixabayFragment || fragment instanceof UnsplashFragment || fragment instanceof PexelsFragment || fragment instanceof SearchFragment) {
            ViewCompat.setElevation(binding.content.appBar, 0);
        } else {
            ViewCompat.setElevation(binding.content.appBar, 4 * getResources().getDisplayMetrics().density);
        }
    }

    private void showStartFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.start_container, GetStartedFragment.newInstance())
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

            case R.id.nav_about:
                replaceFragment(AboutFragment.newInstance(), false);
                break;
        }

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

        replaceFragment(ForYouFragment.newInstance(), false);
    }

    @Override
    public void reCreateFragment(Fragment fragment) {
        replaceFragment(fragment, true);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            frag.setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.move));
            //frag.setEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.slide_right));
            frag.setExitTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.move));
        }

        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_container, frag, frag.getClass().getName())
                .addToBackStack(frag.getClass().getName())
                .addSharedElement(imageView, String.format("transition %s", imageEntity.getId()))
                .commit();

        showToolbar(false);
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
