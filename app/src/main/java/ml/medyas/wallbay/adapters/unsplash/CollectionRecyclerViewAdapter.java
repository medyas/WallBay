package ml.medyas.wallbay.adapters.unsplash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ml.medyas.wallbay.R;
import ml.medyas.wallbay.databinding.CollectionItemLayoutBinding;
import ml.medyas.wallbay.models.CollectionEntity;
import ml.medyas.wallbay.models.unsplash.Tag;

import static ml.medyas.wallbay.models.CollectionEntity.DIFF_CALLBACK;

public class CollectionRecyclerViewAdapter extends PagedListAdapter<CollectionEntity, CollectionRecyclerViewAdapter.CollectionViewHolder> {
    private CollectionInterface mListener;
    private TagsRecyclerViewAdapter.OnTagItemClicked tagListener;
    private Context context;

    public CollectionRecyclerViewAdapter(CollectionInterface listener, TagsRecyclerViewAdapter.OnTagItemClicked tagListener, Context context) {
        super(DIFF_CALLBACK);
        this.mListener = listener;
        this.tagListener = tagListener;
        this.context = context;
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

            if(getItem(i).getTags() != null) {
                List<String> list = new ArrayList<>();
                for (Tag tag : getItem(i).getTags()) {
                    list.add(tag.getTitle());
                }
                TagsRecyclerViewAdapter mAdapter = new TagsRecyclerViewAdapter(list, tagListener);
                holder.itemLayoutBinding.collectionTags.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                holder.itemLayoutBinding.collectionTags.setHasFixedSize(true);
                holder.itemLayoutBinding.collectionTags.setAdapter(mAdapter);

            }
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
