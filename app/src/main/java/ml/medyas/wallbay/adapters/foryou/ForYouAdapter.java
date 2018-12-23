package ml.medyas.wallbay.adapters.foryou;

import android.arch.paging.PagedListAdapter;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ml.medyas.wallbay.R;
import ml.medyas.wallbay.databinding.ImageEntityItemBinding;
import ml.medyas.wallbay.entities.ImageEntity;

public class ForYouAdapter extends PagedListAdapter<ImageEntity, ForYouAdapter.ForYouViewHolder> {
    private onImageItemClicked mListener;

    public ForYouAdapter(onImageItemClicked listener) {
        super(ImageEntity.DIFF_CALLBACK);
        mListener = listener;
    }

    @NonNull
    @Override
    public ForYouViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        ImageEntityItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.image_entity_item, parent, false);
        return new ForYouViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ForYouViewHolder holder, int i) {

        if (getItem(i) == null) {

        } else {
            holder.imageEntityItemBinding.setImageItem(getItem(i));
            holder.imageEntityItemBinding.itemAddToFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onAddToFavorite(getItem(holder.getAdapterPosition()));
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClicked(getItem(holder.getAdapterPosition()), holder.imageEntityItemBinding.itemImage);
                }
            });
        }
    }

    public interface onImageItemClicked {

        void onAddToFavorite(ImageEntity position);

        void onItemClicked(ImageEntity item, ImageView itemImage);
    }

    public class ForYouViewHolder extends RecyclerView.ViewHolder {
        private ImageEntityItemBinding imageEntityItemBinding;

        public ForYouViewHolder(@NonNull ImageEntityItemBinding itemView) {
            super(itemView.getRoot());
            imageEntityItemBinding = itemView;
        }
    }
}
