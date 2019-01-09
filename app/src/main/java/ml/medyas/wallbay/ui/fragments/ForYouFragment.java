package ml.medyas.wallbay.ui.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import ml.medyas.wallbay.R;
import ml.medyas.wallbay.adapters.ActionModeCallback;
import ml.medyas.wallbay.adapters.foryou.ForYouAdapter;
import ml.medyas.wallbay.databinding.FragmentForYouBinding;
import ml.medyas.wallbay.entities.ImageEntity;
import ml.medyas.wallbay.models.foryou.ForYouViewModel;
import ml.medyas.wallbay.models.foryou.ForYouViewModelFactory;
import ml.medyas.wallbay.services.WallpaperService;
import ml.medyas.wallbay.utils.GlideApp;
import ml.medyas.wallbay.utils.Utils;

import static ml.medyas.wallbay.utils.Utils.INTEREST_CATEGORIES;
import static ml.medyas.wallbay.utils.Utils.calculateNoOfColumns;
import static ml.medyas.wallbay.utils.Utils.convertPixelsToDp;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnForYouFragmentInteractions} interface
 * to handle interaction events.
 * Use the {@link ForYouFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForYouFragment extends Fragment implements ForYouAdapter.onImageItemClicked, ActionModeCallback.onActionModeInterface {
    private OnForYouFragmentInteractions mListener;
    private ForYouViewModel mViewModel;
    private ForYouAdapter mAdapter;
    private FragmentForYouBinding binding;
    public static boolean inSelection = false;
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;

    private TextView sheetText;
    private ImageView sheetImage;

    public ForYouFragment() {
        // Required empty public constructor
    }

    public static ForYouFragment newInstance() {
        return new ForYouFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        setUpViewModel();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_for_you, container, false);

        mAdapter = new ForYouAdapter(this);

        actionModeCallback = new ActionModeCallback(this, true);
        binding.statusLayout.slideShowPlay.hide();

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(calculateNoOfColumns(getContext(), convertPixelsToDp(getResources().getDimension(R.dimen.item_width), getContext())), StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

        binding.forYouRecyclerView.setLayoutManager(layoutManager);
        binding.forYouRecyclerView.setHasFixedSize(false);
        binding.forYouRecyclerView.setAdapter(mAdapter);
        binding.forYouRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                if (mAdapter.getCurrentList().size() == 0) {
                    Toast.makeText(getContext(), "Wait for images to load!", Toast.LENGTH_SHORT).show();
                } else {

                }
            }
        });

        return binding.getRoot();
    }

    private void setUpViewModel() {

        if (mViewModel == null) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
            String interests = pref.getString(INTEREST_CATEGORIES, "");
            if (interests != null && !interests.equals("")) {
                mViewModel = ViewModelProviders.of(this, new ForYouViewModelFactory(interests)).get(ForYouViewModel.class);
            }
        }


        mViewModel.getPagedListLiveData().observe(this, new Observer<PagedList<ImageEntity>>() {
            @Override
            public void onChanged(@Nullable PagedList<ImageEntity> imageEntities) {
                mAdapter.submitList(imageEntities);
            }
        });

        mViewModel.getNetworkStateLiveData().observe(this, new Observer<Utils.NetworkState>() {
            @Override
            public void onChanged(@Nullable Utils.NetworkState networkState) {
                if (networkState == Utils.NetworkState.LOADED) {
                    binding.statusLayout.itemLoad.setVisibility(View.GONE);
                    binding.forYouRecyclerView.setVisibility(View.VISIBLE);

                } else if (networkState == Utils.NetworkState.LOADING) {

                } else if (networkState == Utils.NetworkState.FAILED) {
                    if (mAdapter.getCurrentList().size() == 0) {
                        binding.statusLayout.netError.setVisibility(View.VISIBLE);
                        binding.statusLayout.itemLoad.setVisibility(View.GONE);
                        Snackbar.make(binding.statusLayout.netError, "Network Error", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Retray", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        binding.statusLayout.netError.setVisibility(View.GONE);
                                        binding.statusLayout.itemLoad.setVisibility(View.VISIBLE);

                                        mListener.reCreateFragment(ForYouFragment.newInstance());
                                    }
                                }).show();
                    } else {
                        Snackbar.make(binding.statusLayout.netError, "Failed to load more data", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_activity_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_favorite) {
            mListener.onShowFavoriteFragment();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnForYouFragmentInteractions) {
            mListener = (OnForYouFragmentInteractions) context;
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
    public void onItemClicked(ImageEntity imageEntity, ImageView itemImage, int adapterPosition) {
        if (inSelection) {
            if (actionMode != null) {
                toggleSelection(adapterPosition);
            }
        } else {
            mListener.onItemClicked(imageEntity, adapterPosition, itemImage);
        }
    }

    @Override
    public boolean onItemLongClicked(int position) {
        inSelection = true;
        mAdapter.notifyDataSetChanged();
        binding.statusLayout.slideShowPlay.hide();

        if (!binding.selectedSheet.isInflated()) {
            binding.selectedSheet.getViewStub().inflate();
            binding.selectedSheet.getRoot().setVisibility(View.VISIBLE);

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
        return true;
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

            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    @Override
    public void onItemAddToFavorite(ImageEntity imageEntity) {
        mListener.onAddToFavorite(imageEntity).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                Toast.makeText(getContext(), "Added to favorite", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    /*
            ActionMode callbacks
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
        List<ImageEntity> imageEntities = new ArrayList<>();
        for (Integer i : mAdapter.getSelectedItems()) {
            imageEntities.add(mAdapter.getCurrentList().get(i));
        }
        mListener.onAddToFavorite(imageEntities).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                Toast.makeText(getContext(), getString(R.string.add_succ), Toast.LENGTH_SHORT).show();
                actionMode.finish();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getContext(), getString(R.string.add_error), Toast.LENGTH_SHORT).show();
                actionMode.finish();
            }
        });
    }

    @Override
    public void onMenuDownClicked() {
        List<String> urls = new ArrayList<>();
        for (Integer i : mAdapter.getSelectedItems()) {
            urls.add(mAdapter.getCurrentList().get(i).getOriginalUrl());
        }
        WallpaperService.bulkWallpaperDownload(getContext(), urls);
        Toast.makeText(getContext(), "Downloading images...", Toast.LENGTH_SHORT).show();
        actionMode.finish();
    }

    @Override
    public void onMenuRemoveClicked() {
        // Leave empty
    }

    @Override
    public void onSelectAll() {
        mAdapter.selectAll();
        sheetText.setText(String.valueOf(mAdapter.getSelectedItemCount()));
        GlideApp.with(this)
                .load(mAdapter.getLastSelectedItem())
                .into(sheetImage);
        actionMode.setTitle(String.valueOf(mAdapter.getSelectedItemCount()));
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
    public interface OnForYouFragmentInteractions {
        Completable onAddToFavorite(ImageEntity imageEntity);

        Completable onAddToFavorite(List<ImageEntity> imageEntities);

        void onItemClicked(ImageEntity imageEntity, int position, ImageView imageView);

        void onShowFavoriteFragment();

        void onHideToolbar(boolean hide);

        void reCreateFragment(Fragment fragment);
    }
}
