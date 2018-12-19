package ml.medyas.wallbay.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import io.reactivex.disposables.Disposable;
import ml.medyas.wallbay.R;
import ml.medyas.wallbay.entities.SearchEntity;
import ml.medyas.wallbay.models.SearchViewModel;

public class MainActivity extends AppCompatActivity {

    private SearchViewModel mViewModel;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewModel = ViewModelProviders.of(this)
                .get(SearchViewModel.class);

        getData();

        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page += 1;
                getData();
            }
        });

    }

    private void getData() {
        final String tag = getClass().getName();

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
}
