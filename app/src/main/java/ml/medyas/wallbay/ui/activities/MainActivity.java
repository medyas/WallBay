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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import io.reactivex.disposables.Disposable;
import ml.medyas.wallbay.R;
import ml.medyas.wallbay.databinding.ActivityMainBinding;
import ml.medyas.wallbay.entities.ImageEntity;
import ml.medyas.wallbay.entities.SearchEntity;
import ml.medyas.wallbay.models.SearchViewModel;
import ml.medyas.wallbay.ui.fragments.ForYouFragment;
import ml.medyas.wallbay.ui.fragments.GetStartedFragment;
import ml.medyas.wallbay.ui.fragments.ImageDetailsFragment;

import static ml.medyas.wallbay.utils.Utils.INTEREST_CATEGORIES;

public class MainActivity extends AppCompatActivity implements GetStartedFragment.OnGetStartedFragmentInteractions, ForYouFragment.OnForYouFragmentInteractions,
        ImageDetailsFragment.OnImageDetailsFragmentInteractions {

    public static final String FIRST_START = "first_start";
    public static final String TOOLBAR_VISIBILITY = "toolbar_visibility";
    private SearchViewModel mViewModel;
    private int page = 1;

    private ActivityMainBinding binding;

    //TODO 1: Create fragments UI and communication with viewmodal ( livedata) for : For You
    //• Pixabay
    //• Pexels
    //• Unsplash
    //• Search
    //MainActivity :
    //• Creating the menu
    //• Setting the navigation between fragmnets

    //TODO add recyclerview item animation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.content.toolbar);

        if (savedInstanceState == null) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            if (pref.getBoolean("first_start", true)) {
                binding.startContainer.setVisibility(View.VISIBLE);
                showStartFragment();
            } else {
                binding.content.coordinator.setVisibility(View.VISIBLE);
                replaceFragment(ForYouFragment.newInstance());
            }
            setUpToolbar(true);
        } else {
            binding.content.coordinator.setVisibility(View.VISIBLE);
            setUpToolbar(savedInstanceState.getBoolean(TOOLBAR_VISIBILITY));
        }

        binding.content.toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);
        binding.content.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawerLayout.openDrawer(Gravity.START);
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(TOOLBAR_VISIBILITY, binding.content.toolbar.getVisibility() == View.VISIBLE);
    }

    private void setUpToolbar(boolean b) {
        if (b) {
            binding.content.toolbar.setVisibility(View.VISIBLE);

        } else {
            binding.content.toolbar.setVisibility(View.GONE);
        }
    }

    private void showStartFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.start_container, GetStartedFragment.newInstance())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_favorite) {
            //TODO: create favorite activity
        }
        return super.onOptionsItemSelected(item);
    }


    private void replaceFragment(Fragment fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.setSharedElementReturnTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.slide_right));
            fragment.setExitTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.slide_left));
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, fragment, fragment.getClass().getName())
                .commit();
    }

    private void getData() {
        final String tag = getClass().getName();
        mViewModel = ViewModelProviders.of(this)
                .get(SearchViewModel.class);

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

        Log.d(getClass().getName(), categories);
        binding.content.coordinator.setVisibility(View.VISIBLE);

        replaceFragment(ForYouFragment.newInstance());
    }


    @Override
    public void onImageClicked(ImageEntity imageEntity) {

    }

    @Override
    public void onSetOnBackToolbar(boolean setup) {
        setUpToolbar(!setup);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onImageBackPressed() {
        onBackPressed();
        setUpToolbar(true);
    }
}
