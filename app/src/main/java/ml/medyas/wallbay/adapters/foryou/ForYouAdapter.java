package ml.medyas.wallbay.adapters.foryou;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import ml.medyas.wallbay.R;
import ml.medyas.wallbay.adapters.SelectableClass;
import ml.medyas.wallbay.databinding.ImageEntityItemBinding;
import ml.medyas.wallbay.entities.ImageEntity;
import ml.medyas.wallbay.ui.fragments.ForYouFragment;

public class ForYouAdapter extends SelectableClass {
    private onImageItemClicked mListener;

    public ForYouAdapter(onImageItemClicked listener) {
        mListener = listener;
        setAdapter(this);
    }

    @NonNull
    @Override
    public ForYouViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        ImageEntityItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.image_entity_item, parent, false);
        return new ForYouViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final ForYouViewHolder holder = (ForYouViewHolder) viewHolder;

        if (getItem(i) != null) {
            holder.imageEntityItemBinding.setImageItem(getItem(i));

            if (isAddedToFav(getItem(holder.getAdapterPosition()).getId())) {
                Log.d("mainactivity", getItem(holder.getAdapterPosition()).getId() + " - " + i);
                holder.imageEntityItemBinding.itemAddToFav.setVisibility(View.GONE);
            }
            holder.imageEntityItemBinding.itemAddToFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isAddedToFav(getItem(holder.getAdapterPosition()).getId())) {
                        mListener.onItemAddToFavorite(getItem(holder.getAdapterPosition()));
                        addToFav(getItem(holder.getAdapterPosition()).getId(), holder.getAdapterPosition());
                        holder.imageEntityItemBinding.itemAddToFav.setVisibility(View.GONE);
                    }
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("mainactivity", getItem(holder.getAdapterPosition()).getId() + " - " + holder.getAdapterPosition());
                    mListener.onItemClicked(getItem(holder.getAdapterPosition()), holder.imageEntityItemBinding.itemImage, holder.getAdapterPosition());
                    if (ForYouFragment.inSelection) {
                        holder.imageEntityItemBinding.itemAddToFav.setVisibility(View.GONE);
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        params.setMargins(12, 12, 12, 12);
                        holder.imageEntityItemBinding.itemImage.setLayoutParams(params);
                        holder.itemView.setBackgroundColor(holder.imageEntityItemBinding.itemContent.getResources().getColor(R.color.colorAccent));
                    }
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return mListener.onItemLongClicked(holder.getAdapterPosition());
                }
            });

            if (!ForYouFragment.inSelection) {
                holder.imageEntityItemBinding.itemAddToFav.setVisibility(View.VISIBLE);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params.setMargins(0, 0, 0, 0);
                holder.imageEntityItemBinding.itemImage.setLayoutParams(params);
                holder.itemView.setBackgroundColor(holder.imageEntityItemBinding.itemContent.getResources().getColor(android.R.color.white));
            } else {
                holder.imageEntityItemBinding.itemAddToFav.setVisibility(View.GONE);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                if (isSelected(holder.getAdapterPosition())) {
                    params.setMargins(12, 12, 12, 12);
                    holder.itemView.setBackgroundColor(holder.imageEntityItemBinding.itemContent.getResources().getColor(R.color.colorAccent));
                } else {
                    params.setMargins(8, 8, 8, 8);
                    holder.itemView.setBackgroundColor(holder.imageEntityItemBinding.itemContent.getResources().getColor(android.R.color.darker_gray));
                }
                holder.imageEntityItemBinding.itemImage.setLayoutParams(params);
            }
        }
    }


    public interface onImageItemClicked {

        void onItemAddToFavorite(ImageEntity position);

        void onItemClicked(ImageEntity item, ImageView itemImage, int adapterPosition);

        boolean onItemLongClicked(int position);
    }

    public class ForYouViewHolder extends RecyclerView.ViewHolder {
        private ImageEntityItemBinding imageEntityItemBinding;

        public ForYouViewHolder(@NonNull ImageEntityItemBinding itemView) {
            super(itemView.getRoot());
            imageEntityItemBinding = itemView;
        }
    }
}
