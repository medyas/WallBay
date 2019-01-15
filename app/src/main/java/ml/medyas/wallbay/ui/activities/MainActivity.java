package ml.medyas.wallbay.ui.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
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
import ml.medyas.wallbay.ui.fragments.PexelsFragment;
import ml.medyas.wallbay.ui.fragments.PixabayFragment;
import ml.medyas.wallbay.ui.fragments.PixabayViewPagerFragment;
import ml.medyas.wallbay.ui.fragments.SearchFragment;
import ml.medyas.wallbay.ui.fragments.UnsplashCollectionsFragment;
import ml.medyas.wallbay.ui.fragments.UnsplashDefaultVPFragment;
import ml.medyas.wallbay.ui.fragments.UnsplashFragment;

import static ml.medyas.wallbay.utils.Utils.INTEREST_CATEGORIES;

public class MainActivity extends AppCompatActivity implements GetStartedFragment.OnGetStartedFragmentInteractions, BaseFragment.OnBaseFragmentInteractions,
        ImageDetailsFragment.OnImageDetailsFragmentInteractions, FavoriteFragment.onFavoriteFragmentInteractions, PixabayFragment.OnPixabayFragmentInteractions,
        UnsplashFragment.OnUnsplashFragmentInteractions, PexelsFragment.OnFragmentInteractionListener, SearchFragment.OnFragmentInteractionListener,
    AboutFragment.OnFragmentInteractionListener, ForYouFragment.OnForYouFragmentInteractions, UnsplashCollectionsFragment.UnsplashCollectionInterface {

    public static final String FIRST_START = "first_start";
    public static final String TOOLBAR_VISIBILITY = "toolbar_visibility";
    public static final String FAVORITE_SHOWN = "FAVORITE_SHOWN";

    private ActivityMainBinding binding;
    private FavoriteViewModel favoriteViewModel;
    private boolean addedFragmentShown = false;

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
            //getSupportActionBar().setTitle(getString(R.string.app_name));
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
                    setUpToolbar(true);
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
    }

    @Override
    public void onBackPressed() {
        String fragment = getSupportFragmentManager().findFragmentById(R.id.main_container).getClass().getName();
        if (fragment.equals(ImageDetailsFragment.TAG)) {
            showToolbar(true);
        } else if (fragment.equals(FavoriteFragment.TAG) ||
                        fragment.equals(PixabayViewPagerFragment.TAG) ||
                        fragment.equals(UnsplashDefaultVPFragment.TAG) ) {
            setUpToolbar(addedFragmentShown);
            binding.content.toolbar.inflateMenu(R.menu.main_activity_menu);
        }

        super.onBackPressed();
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
        return favoriteViewModel.insertFavorite(imageEntity);
    }

    @Override
    public Completable onAddToFavorite(List<ImageEntity> imageEntities) {
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

        showToolbar(false);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_container, frag, frag.getClass().getName())
                .addToBackStack(frag.getClass().getName())
                .addSharedElement(imageView, String.format("transition %s", imageEntity.getId()))
                .commit();
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

        if(fragment instanceof FavoriteFragment) {
            getSupportActionBar().setTitle(getString(R.string.favorite));
        }
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
