package ml.medyas.wallbay.adapters.unsplash;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ml.medyas.wallbay.R;

public class TagsRecyclerViewAdapter extends RecyclerView.Adapter<TagsRecyclerViewAdapter.TagsRecyclerViewViewHolder> {
    private List<String> items;
    private OnTagItemClicked mListener;

    public interface OnTagItemClicked {
        void onTagItemClicked(String query);
    }

    public TagsRecyclerViewAdapter(List<String> items, OnTagItemClicked mListener) {
        this.items = items;
        this.mListener = mListener;
    }

    @Override
    public TagsRecyclerViewViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tags_layout_item, parent, false);
        return new TagsRecyclerViewViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TagsRecyclerViewViewHolder holder, int position) {
        final String item = items.get(position);

        holder.itemTag.setText(item);
        holder.itemTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onTagItemClicked(item);
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

    public class TagsRecyclerViewViewHolder extends RecyclerView.ViewHolder {
        private TextView itemTag;

        public TagsRecyclerViewViewHolder(View itemView) {
            super(itemView);

            itemTag = itemView.findViewById(R.id.item_tag);
        }
    }
}