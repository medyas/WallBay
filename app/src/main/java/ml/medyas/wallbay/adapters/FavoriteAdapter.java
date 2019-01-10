package ml.medyas.wallbay.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import ml.medyas.wallbay.R;
import ml.medyas.wallbay.databinding.FavoriteImageEntityItemBinding;
import ml.medyas.wallbay.entities.ImageEntity;
import ml.medyas.wallbay.ui.fragments.FavoriteFragment;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    private List<ImageEntity> imageEntities = new ArrayList<>();
    private onFavItemClicked mListener;
    private SparseBooleanArray selectedItems;


    public FavoriteAdapter(List<ImageEntity> imageEntities, onFavItemClicked mListener) {
        this.imageEntities = imageEntities;
        selectedItems = new SparseBooleanArray();
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        FavoriteImageEntityItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.favorite_image_entity_item, viewGroup, false);
        return new FavoriteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavoriteViewHolder holder, int i) {
        holder.itemBinding.setImageItem(imageEntities.get(i));

        holder.itemBinding.itemRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemRemove(holder.getAdapterPosition(), imageEntities.get(holder.getAdapterPosition()));
                if (FavoriteFragment.inSelection) {
                    holder.itemBinding.itemRemove.setVisibility(View.GONE);
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    params.setMargins(12, 12, 12, 12);
                    holder.itemBinding.itemImage.setLayoutParams(params);
                    holder.itemView.setBackgroundColor(holder.itemBinding.itemContent.getResources().getColor(R.color.colorAccent));
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClicked(holder.getAdapterPosition(), imageEntities.get(holder.getAdapterPosition()), holder.itemBinding.itemImage);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mListener.onItemLongClick(holder.getAdapterPosition());
                return true;
            }
        });

        if (!FavoriteFragment.inSelection) {
            holder.itemBinding.itemRemove.setVisibility(View.VISIBLE);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.setMargins(0, 0, 0, 0);
            holder.itemBinding.itemImage.setLayoutParams(params);
            holder.itemView.setBackgroundColor(holder.itemBinding.itemContent.getResources().getColor(android.R.color.white));
        } else {
            holder.itemBinding.itemRemove.setVisibility(View.GONE);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            if (isSelected(holder.getAdapterPosition())) {
                params.setMargins(12, 12, 12, 12);
                holder.itemView.setBackgroundColor(holder.itemBinding.itemContent.getResources().getColor(R.color.colorAccent));
            } else {
                params.setMargins(8, 8, 8, 8);
                holder.itemView.setBackgroundColor(holder.itemBinding.itemContent.getResources().getColor(android.R.color.darker_gray));
            }
            holder.itemBinding.itemImage.setLayoutParams(params);
        }


    }

    @Override
    public int getItemCount() {
        if (imageEntities != null)
            return imageEntities.size();
        return 0;
    }

    public List<ImageEntity> getImageEntities() {
        return imageEntities;
    }

    /**
     * Indicates if the item at position position is selected
     *
     * @param position Position of the item to check
     * @return true if the item is selected, false otherwise
     */
    public boolean isSelected(int position) {
        return getSelectedItems().contains(position);
    }

    /**
     * Toggle the selection status of the item at a given position
     *
     * @param position Position of the item to toggle the selection status for
     */
    public void toggleSelection(int position) {

        if (selectedItems.get(position, false)) {
            selectedItems.delete(position);
        } else {
            selectedItems.put(position, true);
        }
        notifyItemChanged(position);
    }

    /**
     * Select all adapter items
     */

    public void selectAll() {
        if (getItemCount() == getSelectedItems().size()) {
            selectedItems.clear();
        } else {
            for (int i = 0; i < getItemCount(); i++) {
                selectedItems.put(i, true);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * Clear the selection status for all items
     */
    public void clearSelection() {
        List<Integer> selection = getSelectedItems();
        selectedItems.clear();
        for (Integer i : selection) {
            notifyItemChanged(i);
        }
    }

    /**
     * Count the selected items
     *
     * @return Selected items count
     */
    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    /**
     * Indicates the list of selected items
     *
     * @return List of selected items ids
     */
    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); ++i) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    public void setSelectedItems(SparseBooleanArray selectedItems) {
        this.selectedItems = selectedItems;
    }

    public String getLastSelectedItem() {
        if (getSelectedItems().size() == 0) {
            return "";
        }
        ImageEntity imageEntity = imageEntities.get(selectedItems.keyAt(selectedItems.size() - 1));
        return imageEntity.getPreviewImage();
    }

    public void clearItems() {
        this.imageEntities.clear();
    }

    public interface onFavItemClicked {
        void onItemClicked(int position, ImageEntity imageEntity, ImageView imageView);

        void onItemRemove(int position, ImageEntity imageEntity);

        void onItemLongClick(int position);
    }


    public class FavoriteViewHolder extends RecyclerView.ViewHolder {
        private FavoriteImageEntityItemBinding itemBinding;

        public FavoriteViewHolder(@NonNull FavoriteImageEntityItemBinding itemView) {
            super(itemView.getRoot());
            itemBinding = itemView;
        }
    }
}
