package ml.medyas.wallbay.ui.photoEditor.filters;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ml.medyas.wallbay.R;
import ml.medyas.wallbay.databinding.RowFilterViewBinding;
import ml.medyas.wallbay.utils.GlideApp;

import static ml.medyas.wallbay.utils.Utils.photoFilters;

/**
 * @author <a href="https://github.com/burhanrashid52">Burhanuddin Rashid</a>
 * @version 0.1.2
 * @since 5/23/2018
 */
public class FilterViewAdapter extends RecyclerView.Adapter<FilterViewAdapter.ViewHolder> {

    private FilterListener mFilterListener;
    private String imageUrl;
    private Context context;

    public FilterViewAdapter(FilterListener filterListener, String url, Context context) {
        mFilterListener = filterListener;
        imageUrl = url;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowFilterViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_filter_view, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.rowFilterViewBinding.setText(photoFilters[position].name().replace("_", " "));
        holder.rowFilterViewBinding.setUrl(imageUrl);
    }

    @Override
    public int getItemCount() {
        return photoFilters.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RowFilterViewBinding rowFilterViewBinding;

        ViewHolder(RowFilterViewBinding itemView) {
            super(itemView.getRoot());
            rowFilterViewBinding = itemView;
            itemView.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFilterListener.onFilterSelected(photoFilters[getLayoutPosition()]);
                }
            });
        }
    }

    @BindingAdapter({"loadEditorImage"})
    public static void loadEditorImage(PhotoEditorView view, String url) {
        GlideApp.with(view)
                .load(url)
                .into(view.getSource());
    }
}
