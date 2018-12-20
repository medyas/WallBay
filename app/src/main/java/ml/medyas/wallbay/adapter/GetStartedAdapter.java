package ml.medyas.wallbay.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ml.medyas.wallbay.R;
import ml.medyas.wallbay.databinding.GetStartedItemLayoutBinding;
import ml.medyas.wallbay.entities.GetStartedEntity;

public class GetStartedAdapter extends RecyclerView.Adapter<GetStartedAdapter.GetStartedItemViewholder> {
    private final Context context;
    private List<GetStartedEntity> items;

    public GetStartedAdapter(List<GetStartedEntity> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public GetStartedItemViewholder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        GetStartedItemLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.get_started_item_layout, parent, false);
        return new GetStartedItemViewholder(binding);
    }

    @Override
    public void onBindViewHolder(GetStartedItemViewholder holder, int position) {
        GetStartedEntity item = items.get(position);
        holder.itemLayoutBinding.setCategory(item);
        holder.itemLayoutBinding.categoryItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    public class GetStartedItemViewholder extends RecyclerView.ViewHolder {
        private GetStartedItemLayoutBinding itemLayoutBinding;

        public GetStartedItemViewholder(GetStartedItemLayoutBinding itemView) {
            super(itemView.getRoot());
            itemLayoutBinding = itemView;
        }
    }
}