package ml.medyas.wallbay.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import ml.medyas.wallbay.R;
import ml.medyas.wallbay.adapters.ActionModeCallback;
import ml.medyas.wallbay.adapters.FavoriteAdapter;
import ml.medyas.wallbay.databinding.FragmentFavoriteBinding;
import ml.medyas.wallbay.models.ImageEntity;
import ml.medyas.wallbay.services.WallpaperService;
import ml.medyas.wallbay.utils.GlideApp;
import ml.medyas.wallbay.viewmodels.FavoriteViewModel;
import ml.medyas.wallbay.widget.WallbayWidget;

import static ml.medyas.wallbay.utils.Utils.calculateNoOfColumns;
import static ml.medyas.wallbay.utils.Utils.convertPixelsToDp;


public class FavoriteFragment extends Fragment implements FavoriteAdapter.onFavItemClicked, ActionModeCallback.onActionModeInterface {

    public static boolean inSelection = false;
    private onFavoriteFragmentInteractions mListener;
    private FavoriteViewModel favoriteViewModel;
    private List<ImageEntity> imageEntities = new ArrayList<>();
    private FragmentFavoriteBinding binding;
    private FavoriteAdapter mAdapter;
    private ActionMode actionMode;
    private ActionModeCallback actionModeCallback;

    public static final String TAG = "ml.medyas.wallbay.ui.fragments.FavoriteFragment";

    private TextView sheetText;
    private ImageView sheetImage;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new FavoriteAdapter(imageEntities, this);
        actionModeCallback = new ActionModeCallback(this, false);

        setUpViewModel();

    }

    private void setUpViewModel() {
        favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);
        favoriteViewModel.getFavorites().observe(this, new Observer<List<ImageEntity>>() {
            @Override
            public void onChanged(@Nullable List<ImageEntity> img) {
                if (img == null || img.size() == 0) {
                    binding.statusLayout.itemLoad.setVisibility(View.GONE);
                    binding.statusLayout.listEmpty.setVisibility(View.VISIBLE);

                    imageEntities.addAll(img);
                    mAdapter.clearItems();
                    mAdapter.notifyDataSetChanged();

                    return;
                }

                binding.statusLayout.itemLoad.setVisibility(View.GONE);
                binding.statusLayout.listEmpty.setVisibility(View.GONE);

                if (imageEntities != null || imageEntities.size() != 0) {
                    imageEntities.clear();
                }
                imageEntities.addAll(img);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(calculateNoOfColumns(getContext(), convertPixelsToDp(getResources().getDimension(R.dimen.item_width), getContext())), StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

        binding.favRecyclerView.setLayoutManager(layoutManager);
        binding.favRecyclerView.setHasFixedSize(true);
        binding.favRecyclerView.setAdapter(mAdapter);
        binding.favRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!inSelection) {
                    if (dy > 0 && binding.statusLayout.slideShowPlay.getVisibility() == View.VISIBLE) {
                        binding.statusLayout.slideShowPlay.hide();
                    } else if (dy < 0 && binding.statusLayout.slideShowPlay.getVisibility() != View.VISIBLE) {
                        binding.statusLayout.slideShowPlay.show();
                    }
                }
            }
        });

        binding.statusLayout.slideShowPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAdapter.getItemCount() == 0) {
                    Toast.makeText(getContext(), getString(R.string.wait_for_load), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), getString(R.string.not_implemented), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onFavoriteFragmentInteractions) {
            mListener = (onFavoriteFragmentInteractions) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /*
           Adapter callbacks
     */
    @Override
    public void onItemClicked(int position, ImageEntity imageEntity, ImageView imageView) {
        if (inSelection) {
            if (actionMode != null) {
                toggleSelection(position);
            }
        } else {
            mListener.onItemClicked(imageEntity, position, imageView);
        }
    }

    @Override
    public void onItemLongClick(int position) {
        inSelection = true;
        mAdapter.notifyDataSetChanged();
        binding.statusLayout.slideShowPlay.hide();

        if (!binding.selectedSheet.isInflated()) {
            binding.selectedSheet.getViewStub().inflate();

            sheetText = binding.selectedSheet.getRoot().findViewById(R.id.selected_items);
            sheetImage = binding.selectedSheet.getRoot().findViewById(R.id.selected_image);
        }
        binding.selectedSheet.getRoot().setVisibility(View.VISIBLE);

        if (actionMode == null) {
            //actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(actionModeCallback);
            Toolbar toolbar = ((AppCompatActivity) getActivity()).findViewById(R.id.toolbar);
            actionMode = toolbar.startActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        mAdapter.toggleSelection(position);
        int count = mAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            sheetText.setText(String.valueOf(count));
            GlideApp.with(this)
                    .load(mAdapter.getLastSelectedItem())
                    .into(sheetImage);

            actionMode.invalidate();
        }
    }

    @Override
    public void onItemRemove(int position, ImageEntity imageEntity) {
        favoriteViewModel.deleteFavorite(imageEntity).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                Toast.makeText(getContext(), getString(R.string.remove_succ), Toast.LENGTH_SHORT).show();
                WallbayWidget.sendRefreshBroadcast(getContext());
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getContext(), getString(R.string.remove_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
            ActionMode Callbacks
     */
    @Override
    public void onDestroyMode() {
        binding.statusLayout.slideShowPlay.show();
        binding.selectedSheet.getRoot().setVisibility(View.GONE);
        inSelection = false;
        actionMode = null;
        mAdapter.clearSelection();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMenuFavClicked() {
        // Leave empty
    }

    @Override
    public void onMenuDownClicked() {
        List<String> urls = new ArrayList<>();
        for (Integer i : mAdapter.getSelectedItems()) {
            urls.add(mAdapter.getImageEntities().get(i).getOriginalUrl());
        }
        WallpaperService.bulkWallpaperDownload(getContext(), urls);
        Toast.makeText(getContext(), getResources().getString(R.string.downloading), Toast.LENGTH_SHORT).show();
        actionMode.finish();
    }

    @Override
    public void onMenuRemoveClicked() {
        List<ImageEntity> imageEntities = new ArrayList<>();
        for (Integer i : mAdapter.getSelectedItems()) {
            imageEntities.add(mAdapter.getImageEntities().get(i));
        }
        favoriteViewModel.deleteFavorite(imageEntities).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                Toast.makeText(getContext(), getString(R.string.remove_succ), Toast.LENGTH_SHORT).show();

                WallbayWidget.sendRefreshBroadcast(getContext());
                actionMode.finish();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getContext(), getString(R.string.remove_error), Toast.LENGTH_SHORT).show();
                actionMode.finish();
            }
        });
    }

    @Override
    public void onSelectAll() {
        mAdapter.selectAll();
        sheetText.setText(String.valueOf(mAdapter.getSelectedItemCount()));
        GlideApp.with(this)
                .load(mAdapter.getLastSelectedItem())
                .into(sheetImage);
        //actionMode.setTitle(String.valueOf(mAdapter.getSelectedItemCount()));
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface onFavoriteFragmentInteractions {
        void onItemClicked(ImageEntity imageEntity, int position, ImageView imageView);
    }
}
