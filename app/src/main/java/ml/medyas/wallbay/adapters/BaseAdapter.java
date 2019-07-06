package ml.medyas.wallbay.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import ml.medyas.wallbay.R;
import ml.medyas.wallbay.databinding.ImageEntityItemBinding;
import ml.medyas.wallbay.models.ImageEntity;
import ml.medyas.wallbay.ui.fragments.BaseFragment;
import ml.medyas.wallbay.ui.fragments.ForYouFragment;

public class BaseAdapter extends SelectableClass {
    private onImageItemClicked mListener;
    private Context context;

    public BaseAdapter(onImageItemClicked listener, Context context) {
        mListener = listener;
        this.context = context;
        setAdapter(this);
        setHasStableIds(true);
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

            holder.imageEntityItemBinding.itemAddToFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isAddedToFav(holder.getAdapterPosition())) {
                        mListener.onItemAddToFavorite(getItem(holder.getAdapterPosition()));
                        addToFav(holder.getAdapterPosition());
                        holder.imageEntityItemBinding.itemAddToFav.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
                    }
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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

            if (!BaseFragment.inSelection) {
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

            if (isAddedToFav(holder.getAdapterPosition())) {
                holder.imageEntityItemBinding.itemAddToFav.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
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
