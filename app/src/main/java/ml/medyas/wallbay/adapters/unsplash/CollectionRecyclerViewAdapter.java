package ml.medyas.wallbay.adapters.unsplash;

import android.arch.paging.PagedListAdapter;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ml.medyas.wallbay.R;
import ml.medyas.wallbay.databinding.CollectionItemLayoutBinding;
import ml.medyas.wallbay.entities.CollectionEntity;

import static ml.medyas.wallbay.entities.CollectionEntity.DIFF_CALLBACK;

public class CollectionRecyclerViewAdapter extends PagedListAdapter<CollectionEntity, CollectionRecyclerViewAdapter.CollectionViewHolder> {
    private CollectionInterface mListener;

    public CollectionRecyclerViewAdapter(CollectionInterface listener) {
        super(DIFF_CALLBACK);
        this.mListener = listener;
    }

    public interface CollectionInterface {
        void onCollectionItemClicked(CollectionEntity collectionEntity);
    }

    @NonNull
    @Override
    public CollectionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CollectionItemLayoutBinding itemLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.collection_item_layout, viewGroup, false);
        return new CollectionViewHolder(itemLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final CollectionViewHolder holder, int i) {
        if(getItem(i) != null) {
            holder.itemLayoutBinding.setCollection(getItem(i));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onCollectionItemClicked(getItem(holder.getAdapterPosition()));
                }
            });
        }
    }

    public class CollectionViewHolder extends RecyclerView.ViewHolder {
        private CollectionItemLayoutBinding itemLayoutBinding;

        public CollectionViewHolder(@NonNull CollectionItemLayoutBinding collectionItemLayoutBinding) {
            super(collectionItemLayoutBinding.getRoot());
            itemLayoutBinding = collectionItemLayoutBinding;
        }
    }
}
