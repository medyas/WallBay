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
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;
import ml.medyas.wallbay.R;
import ml.medyas.wallbay.databinding.ActivityMainBinding;
import ml.medyas.wallbay.databinding.NavigationBaseLayoutBinding;
import ml.medyas.wallbay.entities.ImageEntity;
import ml.medyas.wallbay.entities.SearchEntity;
import ml.medyas.wallbay.models.FavoriteViewModel;
import ml.medyas.wallbay.models.SearchViewModel;
import ml.medyas.wallbay.ui.fragments.AboutFragment;
import ml.medyas.wallbay.ui.fragments.BaseFragment;
import ml.medyas.wallbay.ui.fragments.FavoriteFragment;
import ml.medyas.wallbay.ui.fragments.ForYouFragment;
import ml.medyas.wallbay.ui.fragments.GetStartedFragment;
import ml.medyas.wallbay.ui.fragments.ImageDetailsFragment;
import ml.medyas.wallbay.ui.fragments.PexelsFragment;
import ml.medyas.wallbay.ui.fragments.PixabayFragment;
import ml.medyas.wallbay.ui.fragments.SearchFragment;
import ml.medyas.wallbay.ui.fragments.UnsplashFragment;

import static ml.medyas.wallbay.utils.Utils.INTEREST_CATEGORIES;

public class MainActivity extends AppCompatActivity implements GetStartedFragment.OnGetStartedFragmentInteractions, BaseFragment.OnBaseFragmentInteractions,
        ImageDetailsFragment.OnImageDetailsFragmentInteractions, FavoriteFragment.onFavoriteFragmentInteractions, PixabayFragment.OnPixabayFragmentInteractions,
    UnsplashFragment.OnFragmentInteractionListener, PexelsFragment.OnFragmentInteractionListener, SearchFragment.OnFragmentInteractionListener,
    AboutFragment.OnFragmentInteractionListener, ForYouFragment.OnForYouFragmentInteractions {

    public static final String FIRST_START = "first_start";
    public static final String TOOLBAR_VISIBILITY = "toolbar_visibility";
    public static final String FAVORITE_SHOWN = "FAVORITE_SHOWN";

    private ActivityMainBinding binding;
    private FavoriteViewModel favoriteViewModel;
    private boolean favShown = false;

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
            favShown = false;
            getSupportActionBar().setTitle(getString(R.string.app_name));
            binding.content.toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);
            binding.content.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.drawerLayout.openDrawer(Gravity.START);
                }
            });
        } else {
            favShown = true;
            getSupportActionBar().setTitle(getString(R.string.favorite));
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
        outState.putBoolean(FAVORITE_SHOWN, favShown);
    }

    @Override
    public void onBackPressed() {
        String fragment = getSupportFragmentManager().findFragmentById(R.id.main_container).getClass().getName();
        if (fragment.equals(ImageDetailsFragment.TAG)) {
            showToolbar(true);
        } else if (fragment.equals(FavoriteFragment.TAG)) {
            setUpToolbar(favShown);
            binding.content.toolbar.inflateMenu(R.menu.main_activity_menu);
        }


        super.onBackPressed();
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
                    .replace(R.id.main_container, fragment, fragment.getClass().getName())
                    .commit();
        }
    }

    public void onNavItemClicked(View view) {
        int id = view.getId();
        binding.drawerLayout.closeDrawers();

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
    }

    private void getData() {
        final String tag = getClass().getName();
        SearchViewModel mViewModel = ViewModelProviders.of(this)
                .get(SearchViewModel.class);

        int page = 1;
        mViewModel.getSearchAllEndpoints("nature", page).subscribe(new io.reactivex.Observer<SearchEntity>() {
            @Override
            public void onSubscribe(Disposable d) {

                Log.d("mainactivity", "onSubscribe");
            }

            @Override
            public void onNext(SearchEntity searchEntity) {
                if (searchEntity != null) {
                    Log.d("mainactivity", "Data total size: " + searchEntity.getAll().size());
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();

            }

            @Override
            public void onComplete() {

            }
        });

        /*mViewModel.getUnsplashPhotos("latest", page).observe(this, new Observer<List<ImageEntity>>() {
            @Override
            public void onChanged(@Nullable List<ImageEntity> imageEntities) {
                if (!imageEntities.isEmpty() && imageEntities != null) {
                    Log.d(tag, "UserName is : "+imageEntities.get(0).getUserName() + imageEntities.size());
                    if(imageEntities.get(0).getProvider() == Utils.webSite.EMPTY) {
                        Log.d(tag, "Found Empty Data");
                    } else if(imageEntities.get(0).getProvider() == Utils.webSite.ERROR) {
                        Log.d(tag, "Error Could not get Data !");
                    }
                } else {
                    Log.d(tag, "Null");
                }
            }
        });

        if (mViewModel.getUnsplashSearch("nature", page - 1).hasObservers()) {
            mViewModel.getUnsplashSearch("nature", page - 1).removeObservers(this);
        }
        mViewModel.getPexelsSearch("back", page).observe(this, new Observer<List<ImageEntity>>() {
            @Override
            public void onChanged(@Nullable List<ImageEntity> imageEntities) {
                if (!imageEntities.isEmpty() && imageEntities != null) {
                    Log.d(tag, "UserName is : " + imageEntities.get(0).getUserName() + " " + imageEntities.size());
                    if (imageEntities.get(0).getProvider() == Utils.webSite.EMPTY) {
                        Log.d(tag, "Found Empty Data");
                    } else if (imageEntities.get(0).getProvider() == Utils.webSite.ERROR) {
                        Log.d(tag, "Error Could not get Data !");
                    }
                } else {
                    Log.d(tag, "Null");
                    Log.d(tag, imageEntities.toString());
                }
            }
        });


        mViewModel.getPixabaySearch("", page, "", "", false, "")
                .observe(this, new Observer<List<ImageEntity>>() {
                    @Override
                    public void onChanged(@Nullable List<ImageEntity> imageEntities) {
                        if (!imageEntities.isEmpty() && imageEntities != null) {
                            if (imageEntities.get(0).getProvider() == Utils.webSite.EMPTY) {
                                Log.d(tag, "Found Empty Data");
                            } else if (imageEntities.get(0).getProvider() == Utils.webSite.ERROR) {
                                Log.d(tag, "Error Could not get Data !");
                            } else {

                                Log.d(tag, "UserName is : " + imageEntities.get(0).getUserName() + " " + imageEntities.size());
                            }
                        } else {
                            Log.d(tag, "Null");
                        }
                    }
                });*/
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
    public void onShowFavoriteFragment() {
        Fragment fragment = FavoriteFragment.newInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.setSharedElementReturnTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.slide_right));
            fragment.setExitTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.slide_left));
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_container, fragment, fragment.getClass().getName())
                .addToBackStack(fragment.getClass().getName())
                .commit();
        favShown = true;
        setUpToolbar(false);
    }

    @Override
    public void onImageBackPressed() {
        onBackPressed();
    }
}
